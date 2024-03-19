package icu.takeneko.neko.medibox

import android.content.Intent
import android.nfc.TagLostException
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import icu.takeneko.neko.medibox.activity.CreditsActivity
import icu.takeneko.neko.medibox.activity.DisplayMediBoxActivity
import icu.takeneko.neko.medibox.activity.EditMediBoxActivity
import icu.takeneko.neko.medibox.data.MediBox
import icu.takeneko.neko.medibox.databinding.ActivityMainBinding
import icu.takeneko.neko.medibox.nfc.NfcActivity
import icu.takeneko.neko.medibox.nfc.NfcTag
import icu.takeneko.neko.medibox.util.InvalidMediBoxDataException
import icu.takeneko.neko.medibox.util.format
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import java.io.IOException
import java.nio.ByteBuffer

@OptIn(ExperimentalStdlibApi::class)
class MainActivity : NfcActivity() {

    private lateinit var binding: ActivityMainBinding
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
        ToastUtils.showLong(format(R.string.text_exception_coroutine, this, e.toString()))
        e.printStackTrace()
    }
    private val externalScope: CoroutineScope =
        lifecycleScope + coroutineExceptionHandler
    private var readTag = false
    private var readTagCallback: (MediBox) -> Unit = {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.textCredits.setOnClickListener {
            ActivityUtils.startActivity(CreditsActivity::class.java)
        }
        binding.buttonRead.setOnClickListener {
            readTagCallback = {
                readTag = false
                currentMediBox = it
                val intent = Intent(this, DisplayMediBoxActivity::class.java)
                intent.putExtra("preview", false)
                startActivity(intent)
            }
            showWaitingDialog(R.string.text_waiting_for_tag, cancellable = true) {_,_ ->
                readTag = false
                currentWaitingDialog?.dismiss()
            }
            readTag = true
        }
        binding.buttonCreate.setOnClickListener {
            currentMediBox = MediBox(mutableMapOf(), mutableListOf())
            ActivityUtils.startActivity(EditMediBoxActivity::class.java)
        }
        binding.buttonModify.setOnClickListener {
            readTagCallback = {
                readTag = false
                currentMediBox = it
                ActivityUtils.startActivity(EditMediBoxActivity::class.java)
            }
            showWaitingDialog(R.string.text_waiting_for_tag, cancellable = true) {_,_ ->
                readTag = false
                currentWaitingDialog?.dismiss()
            }
            readTag = true
        }
        setContentView(binding.root)
    }

    override fun onNewTag(tag: NfcTag) {
        if (!readTag) return
        Toast.makeText(
            this,
            format(
                R.string.text_new_tag,
                tag.id.toHexString(HexFormat.UpperCase)
            ),
            Toast.LENGTH_SHORT
        ).show()
        showWaitingDialog(R.string.text_reading_tag)
        externalScope.launch(Dispatchers.IO) {
            try {
                tag.connect()
                val blocks = tag.determineWritableBlocks()
                val content = tag.readBlocksToByteArray(*(blocks.toIntArray()))
                val byteBuffer = ByteBuffer.wrap(content)
                val versionInt = byteBuffer.getInt()
                if (mediBoxVersion != versionInt) {
                    launch(Dispatchers.Main) {
                        showErrorDialog(
                            format(R.string.text_version_mismatch),
                            format(R.string.text_version_requirement, mediBoxVersion, versionInt),
                            R.string.label_ok
                        )
                    }
                    return@launch
                }
                val mediBox = MediBox.decode(byteBuffer)
                launch(Dispatchers.Main) {
                    currentWaitingDialog?.dismiss()
                    readTagCallback(mediBox)
                }
            } catch (e: IOException) {
                launch(Dispatchers.Main) {
                    showErrorDialog(
                        R.string.text_nfc_failure,
                        R.string.text_nfc_io_error,
                        R.string.label_ok
                    )
                }
            } catch (e: TagLostException) {
                launch(Dispatchers.Main) {
                    showErrorDialog(
                        R.string.text_nfc_failure,
                        R.string.text_nfc_tag_lost,
                        R.string.label_ok
                    )
                }
            } catch (e: InvalidMediBoxDataException) {
                val message = when (e.messageResId) {
                    R.string.text_data_length_mismatch -> format(R.string.text_data_length_mismatch)
                    R.string.text_empty_data -> format(R.string.text_empty_data)
                    R.string.text_medicine_not_found -> format(
                        R.string.text_medicine_not_found,
                        buildString {
                            e.missingMedicineList.forEach {
                                // Medicine id %d label usage as %s was not found in MediBox data
                                append(
                                    format(
                                        R.string.text_medicine_not_found_with_usage,
                                        it.medicineId,
                                        it.describeUsage(this@MainActivity)
                                    )
                                )
                                append("\n")
                            }
                        })
                    else -> ""
                }
                showErrorDialog(format(R.string.text_invalid_data), message, R.string.label_ok)
            }
        }
    }
}