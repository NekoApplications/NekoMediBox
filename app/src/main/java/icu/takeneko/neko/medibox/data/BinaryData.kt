package icu.takeneko.neko.medibox.data

import java.nio.ByteBuffer

interface BinaryData<T> {
    fun encode(obj:T): ByteArray
    fun decode(byteBuffer: ByteBuffer): T
}