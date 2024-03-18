package icu.takeneko.neko.medibox

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ToastUtils
import icu.takeneko.neko.medibox.databinding.ActivityMainBinding
import icu.takeneko.neko.medibox.nfc.NfcActivity
import icu.takeneko.neko.medibox.nfc.NfcTag
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onNewTag(tag: NfcTag) {
        if (!readTag)return
        Toast.makeText(
            this,
            format(
                R.string.text_new_tag,
                tag.id.toHexString(HexFormat.UpperCase)
            ),
            Toast.LENGTH_SHORT
        ).show()
    }
}