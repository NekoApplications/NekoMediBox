package icu.takeneko.neko.medibox

import android.content.DialogInterface
import icu.takeneko.neko.medibox.data.MediBox

const val TAG = "NekoMediBox"

var currentMediBox: MediBox? = null

val defaultDialogButtonCallback: DialogInterface.OnClickListener =
    DialogInterface.OnClickListener { _, _ -> }

val mediBoxVersion = 0x0004