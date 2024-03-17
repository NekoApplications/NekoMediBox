package icu.takeneko.neko.medibox

import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ToastUtils
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus

@OptIn(ExperimentalStdlibApi::class)
fun hexView(byteArray: ByteArray): String =
    buildString {
        append("HexView: \n")
        append("          0 1 2 3 4 5 6 7  8 9 A B C D E F\n")
        var ln = 0
        var s = byteArray.toHexString(HexFormat.UpperCase)
        s += "0".repeat(32 - (s.length % 32))
        var line = String()
        for (c in s) {
            if (line.length == 33) {
                append(ln.toHexString(HexFormat.UpperCase).padStart(6, '0')).append("  ")
                    .append(line).append("\n")
                line = String()
                ln += 16
            }
            if (line.length == 16) {
                line += " "
            }
            line += c
        }
        append(ln.toHexString(HexFormat.UpperCase).padStart(6, '0')).append("  ")
            .append(line)
    }

fun Context.format(@StringRes format: Int, vararg objects: Any?): String {
    return getString(format).format(*objects)
}

fun logI(content:String){
    Log.i(TAG, content)
}

fun logW(content:String){
    Log.w(TAG, content)
}
