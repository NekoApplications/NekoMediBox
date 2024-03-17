package icu.takeneko.neko.medibox.nfc

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.tech.NfcA


class NfcService(private val activity: Activity) {
    private lateinit var nfcAdapter: NfcAdapter
    var available = false

    fun setUpNfc() {
        val adapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(activity)
        if (adapter == null) {
            available = false
            throw NfcException("Error: Could not enable the NFC foreground dispatch system, Nfc was turned off or not supported by this device.")
        }
        nfcAdapter = adapter
        val intent = Intent(activity, activity.javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent =
            PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_MUTABLE)
        try {
            nfcAdapter.enableForegroundDispatch(
                activity,
                pendingIntent,
                null,
                arrayOf(arrayOf(NfcA::class.java.name))
            )
        } catch (e: Exception) {
            available = false
            throw NfcException("Error: Could not enable the NFC foreground dispatch system. The activity was not in foreground.", e)
        }
        available = true
    }

    fun finalizeNfc() {
        if (available) {
            available = false
            try {
                nfcAdapter.disableForegroundDispatch(activity)
            }catch (e: Exception){
                throw NfcException(
                    "Error: Could not disable the NFC foreground dispatch system. The activity was not in foreground.",
                    e
                )
            }
        }
    }
}