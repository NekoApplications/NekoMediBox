package icu.takeneko.neko.medibox.util

import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import icu.takeneko.neko.medibox.TAG
import icu.takeneko.neko.medibox.data.MedicineUsage
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.Random

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

fun ByteArrayOutputStream.writeInt(value:Int){
    val buf = ByteBuffer.wrap(ByteArray(Int.SIZE_BYTES))
    buf.putInt(value)
    this.write(buf.array())
}

fun ByteBuffer.getString(): String {
    val length = getInt()
    val array = ByteArray(length)
    val pos = position()
    var index = 0
    for (i in pos until pos + length) {
        array[index++] = this.get()
    }
    return array.decodeToString()
}

fun List<MedicineUsage>.sortByExactOrderDuration(): List<MedicineUsage>{
    return sortedBy { it.orderDurationExactTime.ordinal }
}

fun randomIntId():Int{
    return kotlin.random.Random(System.nanoTime()).nextInt()
}