package icu.takeneko.neko.medibox.nfc

import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Build
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import icu.takeneko.neko.medibox.R
import icu.takeneko.neko.medibox.util.AppActivity
import icu.takeneko.neko.medibox.util.format

open class NfcActivity: AppActivity() {

    protected lateinit var nfcService:NfcService

    override fun onResume() {
        super.onResume()
        nfcService = NfcService(this)
        try {
            nfcService.setUpNfc()
        } catch (e: NfcException) {
            e.printStackTrace()
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.text_nfc_failure)
                .setMessage(e.message)
                .setNegativeButton(R.string.text_exit_app) { _, _ ->
                    finish()
                }
                .setCancelable(false)
                .create()
                .show()
            return
        }
        if (!nfcService.available) {
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.text_nfc_failure)
                .setMessage(
                    format(
                        R.string.text_nfc_state_error,
                        this,
                        nfcService.available.toString()
                    )
                )
                .setNegativeButton(R.string.text_exit_app) { _, _ ->
                    finish()
                }
                .setCancelable(false)
                .create()
                .show()
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.action == NfcAdapter.ACTION_TECH_DISCOVERED) {
            val tag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(NfcAdapter.EXTRA_TAG, Tag::class.java)
            } else {
                intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            } ?: return
            try {
                 onNewTag(NfcTag(tag))
            } catch (e: NfcException) {
                Toast.makeText(
                    this,
                    format(
                        R.string.text_tag_not_supported,
                        tag.id.toHexString(HexFormat.UpperCase)
                    ),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    open fun onNewTag(tag:NfcTag){}

    override fun onPause() {
        super.onPause()
        nfcService.finalizeNfc()
    }
}