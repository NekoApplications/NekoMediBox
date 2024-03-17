package icu.takeneko.neko.medibox.data

import icu.takeneko.neko.medibox.writeInt
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

class MedicineUsage(
    val medicineIndex: Int,
    val usageType: UsageType,
    val count: Int,
    val orderDuration: OrderDuration,
    val orderDurationExactTime: OrderDurationExactTime
) {

    companion object : BinaryData<MedicineUsage> {
        override fun encode(obj: MedicineUsage): ByteArray {
            val os = ByteArrayOutputStream()
            obj.apply {
                os.writeInt(medicineIndex)
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