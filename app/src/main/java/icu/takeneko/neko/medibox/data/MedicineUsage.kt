package icu.takeneko.neko.medibox.data

import android.content.Context
import android.content.SyncRequest
import icu.takeneko.neko.medibox.util.format
import icu.takeneko.neko.medibox.util.writeInt
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

class MedicineUsage(
    val medicineId: Int,
    val usageType: UsageType,
    val count: Int,
    val orderDuration: OrderDuration,
    val orderDurationExactTime: OrderDurationExactTime
) {

    fun describeUsage(context: Context):String=buildString {
        context.run {
            append(format(usageType.resId)).append(", ").append(format(orderDuration.descriptorStringRes))
            if (orderDurationExactTime != OrderDurationExactTime.NONE){
                append(", ")
                append(format(orderDurationExactTime.descriptorStringRes))
            }
        }
    }



    companion object : BinaryData<MedicineUsage> {
        override fun encode(obj: MedicineUsage): ByteArray {
            val os = ByteArrayOutputStream()
            obj.apply {
                os.writeInt(medicineId)
                os.writeInt(usageType.id())
                os.writeInt(count)
                os.writeInt(orderDuration.id())
                os.writeInt(orderDurationExactTime.id())
            }

            return os.toByteArray()
        }

        override fun decode(byteBuffer: ByteBuffer): MedicineUsage {
            val medicineIndex: Int = byteBuffer.getInt()
            val usageType: UsageType = UsageType.PILL.match(byteBuffer.getInt())
            val count: Int = byteBuffer.getInt()
            val orderDuration: OrderDuration = OrderDuration.PRN.match(byteBuffer.getInt())
            val orderDurationExactTime: OrderDurationExactTime = OrderDurationExactTime.AM.match(byteBuffer.getInt())
            return MedicineUsage(medicineIndex, usageType, count, orderDuration, orderDurationExactTime)
        }
    }
}