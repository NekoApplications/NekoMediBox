package icu.takeneko.neko.medibox.nfc

import android.nfc.Tag
import android.nfc.tech.MifareClassic
import android.nfc.tech.NfcA
import icu.takeneko.neko.medibox.util.logI

@OptIn(ExperimentalStdlibApi::class)
class NfcTag(
    private val tag: Tag,
    private val keyA: ByteArray = MifareClassic.KEY_DEFAULT,
    private val keyB: ByteArray = MifareClassic.KEY_DEFAULT
) {
    val id: ByteArray
        get() = tag.id
    private val mifareTag:MifareClassic
    private val nfcATag:NfcA?



    init {
        logI(
            "Tag: ${tag.id.toHexString(HexFormat.UpperCase)}\n" +
                    "techLists: ${tag.techList.joinToString(", ")}"
        )
        if (tag.techList.contains("android.nfc.tech.NfcA")) {
            nfcATag = NfcA.get(tag)
            nfcATag.apply {
                logI("atqa: ${atqa.toHexString()}, sak: ${sak.toHexString()}, timeout: ${this.timeout}, maxTransLength: ${this.maxTransceiveLength}")
            }
        }else{
            nfcATag = null
        }
        if (tag.techList.contains("android.nfc.tech.MifareClassic")) {
            mifareTag = MifareClassic.get(tag)
            mifareTag.apply {
                logI("sectors: $sectorCount, blocks:$blockCount, timeout: ${this.timeout}, maxTransLength: ${this.maxTransceiveLength}")
            }
        } else {
            throw NfcException("Tag Not Supported (Not a MIFARE Classic tag)")
        }
    }

    fun connect() = mifareTag.connect()

    @OptIn(ExperimentalStdlibApi::class)
    fun determineWritableBlocks(): List<Int> {
        logI("Attempt to determine writable blocks")
        val blocks = mutableSetOf<Int>()
        mifareTag.apply {
            for (i in 0 until sectorCount) {
                authSector(i)
                val firstBlock = sectorToBlock(i)
                for (j in firstBlock until firstBlock + 4) {
                    val block = readBlock(j)
                    logI("Block $j has following data:\n${block.toHexString(HexFormat.UpperCase)}")
                    blocks += if (i == 0) {
                        1..2
                    } else {
                        firstBlock..firstBlock + 2
                    }
                }
            }
        }
        return blocks.toList()
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun authSector(i: Int) {
        mifareTag.apply {
            authenticateSectorWithKeyA(i, keyA)
            logI("Auth Sector $i with KeyA: ${keyA.toHexString()}")
            authenticateSectorWithKeyB(i, keyB)
            logI("Auth Sector $i with KeyB: ${keyB.toHexString()}")
        }
    }

    fun readBlock(block: Int): ByteArray {
        authSector(mifareTag.blockToSector(block))
        return mifareTag.readBlock(block)
    }

    fun readBlocksToByteArray(vararg block: Int): ByteArray {
        val blockContents = buildList {
            var authorizedSector = -1
            for (b in block) {
                val sector = mifareTag.blockToSector(b)
                if (authorizedSector != sector) {
                    authorizedSector = sector
                    authSector(authorizedSector)
                }
                logI("Reading block $b in sector $sector")
                this += mifareTag.readBlock(b)
            }
        }
        var length = 0
        blockContents.forEach { length += it.size }
        val array = ByteArray(length)
        var ptr = 0
        for (bytes in blockContents) {
            for (byte in bytes) {
                array[ptr++] = byte
            }
        }
        return array
    }
}