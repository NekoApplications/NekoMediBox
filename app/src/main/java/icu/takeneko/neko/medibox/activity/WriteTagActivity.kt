package icu.takeneko.neko.medibox.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ToastUtils
import icu.takeneko.neko.medibox.MainActivity
import icu.takeneko.neko.medibox.R
import icu.takeneko.neko.medibox.TAG
import icu.takeneko.neko.medibox.currentMediBox
import icu.takeneko.neko.medibox.databinding.ActivityWriteTagBinding
import icu.takeneko.neko.medibox.nfc.NfcActivity
import icu.takeneko.neko.medibox.nfc.NfcTag
import icu.takeneko.neko.medibox.util.format
import icu.takeneko.neko.medibox.util.hexView
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

class WriteTagActivity : NfcActivity() {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
        ToastUtils.showLong(format(R.string.text_exception_coroutine, this, e.toString()))
        e.printStackTrace()
    }
    private val externalScope: CoroutineScope =
        lifecycleScope + coroutineExceptionHandler
    private lateinit var binding: ActivityWriteTagBinding
    private lateinit var data:ByteArray
    private var canNavigateBackToMainActivity = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWriteTagBinding.inflate(layoutInflater)
        setContentView(binding.root)
        externalScope.launch(Dispatchers.IO) {
            launch(Dispatchers.Main) {
                binding.indicator.visibility = View.VISIBLE
            }
            updateStatus(R.string.text_wait)
            delay(500L)// give some delay to ensure status is displayed
            updateStatus(R.string.text_generating_data)
            try{
                delay(250L)
                data = currentMediBox!!.encode()
                updateStatus(R.string.text_waiting_for_tag)
                launch(Dispatchers.Main) {
                    binding.indicator.visibility = View.GONE
                }
            }catch (e:Throwable){
                updateStatus(e.stackTraceToString())
            }
        }
    }

    override fun onNewTag(tag: NfcTag) {
        binding.indicator.visibility = View.VISIBLE
        externalScope.launch(Dispatchers.IO){
            try {
                updateStatus(R.string.text_determining_blocks)
                tag.connect()
                val writableBlocks = tag.determineWritableBlocks()
                updateStatus(
                    format(
                        R.string.text_writable_blocks,
                        writableBlocks.joinToString(", ", "", "")
                    )
                )

                val tagData = ByteArray(data.size + (16 - (data.size % 16)))
                System.arraycopy(data, 0, tagData, 0, data.size)
                Log.i(TAG, "onNewTag:\n ${hexView(tagData)}")
                val blockCount = tagData.size / 16
                val sparseData: Map<Int, ByteArray> = buildMap {
                    var index = 0
                    writableBlocks.forEach {
                        if (index > blockCount - 1)return@buildMap
                        val blockData = ByteArray(16)
                        System.arraycopy(tagData, index * 16, blockData, 0, 16)
                        this[it] = blockData
                        index++
                    }
                }
                sparseData.forEach { i, data ->
                    updateStatus(format(R.string.text_write_block, i))
                    tag.writeBlock(i, data)
                }
                updateStatus(R.string.text_write_done)
                delay(250L)
                val errorBlocks = mutableListOf<Int>()
                sparseData.forEach { i, data ->
                    updateStatus(format(R.string.text_check_block, i))
                    if(!tag.readBlock(i).contentEquals(data)){
                        errorBlocks.add(i)
                    }
                }
                if (errorBlocks.isNotEmpty()){
                    updateStatus(format(R.string.text_corrupted_data, errorBlocks.joinToString(", ", "", "")))
                }else{
                    updateStatus(format(R.string.text_write_success))
                }
                launch(Dispatchers.Main) {
                    binding.indicator.visibility = View.GONE
                    modifyNavigation()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                updateStatus(format(R.string.text_write_error, e.toString()))
                launch(Dispatchers.Main) {
                    binding.indicator.visibility = View.GONE
                }
            }
        }
    }

    private fun modifyNavigation(){
        canNavigateBackToMainActivity = true
    }

    private fun updateStatus(resId:Int){
        updateStatus(format(resId))
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed(){
        if (canNavigateBackToMainActivity){
            val intent = Intent(this, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }else{
            super.onBackPressed()
        }
    }

    private fun updateStatus(s:String){
        externalScope.launch(Dispatchers.Main) {
            binding.textWriteStatus.text = s
        }
    }
}