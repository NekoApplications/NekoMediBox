package icu.takeneko.neko.medibox.util

import android.content.DialogInterface
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.CircularProgressIndicator
import icu.takeneko.neko.medibox.R
import icu.takeneko.neko.medibox.defaultDialogButtonCallback

abstract class AppActivity : AppCompatActivity() {

    protected var currentWaitingDialog: AlertDialog? = null

    fun showQuestionDialog(
        title: String,
        message: String,
        negativeButtonText: Int,
        negativeButtonCallback: DialogInterface.OnClickListener = defaultDialogButtonCallback,
        positiveButtonText: Int,
        positiveButtonCallback: DialogInterface.OnClickListener = defaultDialogButtonCallback
    ): AlertDialog {
        currentWaitingDialog?.dismiss()
        return MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(negativeButtonText, negativeButtonCallback)
            .setPositiveButton(positiveButtonText, positiveButtonCallback)
            .show()
    }

    fun showQuestionDialog(
        title: Int,
        message: Int,
        negativeButtonText: Int,
        negativeButtonCallback: DialogInterface.OnClickListener = defaultDialogButtonCallback,
        positiveButtonText: Int,
        positiveButtonCallback: DialogInterface.OnClickListener = defaultDialogButtonCallback
    ): AlertDialog {
        return showQuestionDialog(
            format(title),
            format(message),
            negativeButtonText,
            negativeButtonCallback,
            positiveButtonText,
            positiveButtonCallback
        )
    }

    fun showErrorDialog(
        title: String,
        message: String,
        negativeButtonText: Int,
        negativeButtonCallback: DialogInterface.OnClickListener = defaultDialogButtonCallback,
    ): AlertDialog {
        currentWaitingDialog?.dismiss()
        return MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(negativeButtonText, negativeButtonCallback)
            .show()
    }

    fun showErrorDialog(
        title: Int,
        message: Int,
        negativeButtonText: Int,
        negativeButtonCallback: DialogInterface.OnClickListener = defaultDialogButtonCallback,
    ): AlertDialog {
        return showErrorDialog(
            format(title),
            format(message),
            negativeButtonText,
            negativeButtonCallback
        )
    }

    fun showWaitingDialog(
        title: String,
        message: String? = null,
        cancellable: Boolean = false,
        cancellationCallback:DialogInterface.OnClickListener = defaultDialogButtonCallback
    ): AlertDialog {
        currentWaitingDialog?.dismiss()
        val circularProgressIndicator = CircularProgressIndicator(this).apply {
            isIndeterminate = true
            setTheme(R.style.Base_Theme_NekoMediBox)
        }
        return MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .apply {
                if (message != null) setMessage(message)
                if (cancellable) setNegativeButton(R.string.label_cancel, cancellationCallback)
            }
            .setView(LinearLayout(this).apply {
                setPadding(10.dp, 10.dp, 10.dp, 25.dp)
                setLayoutParams(
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                )
                gravity = Gravity.CENTER
                addView(circularProgressIndicator)
            })
            .setCancelable(false)
            .show().also { currentWaitingDialog = it }
    }

    fun showWaitingDialog(
        title: Int,
        message: Int? = null,
        cancellable: Boolean = false,
        cancellationCallback:DialogInterface.OnClickListener = defaultDialogButtonCallback
    ): AlertDialog {
        return showWaitingDialog(
            format(title),
            if (message == null) null else format(message),
            cancellable,
            cancellationCallback
        )
    }

    private val Int.dp
        get() = (resources.displayMetrics.density * this + 0.5f).toInt()
}