package icu.takeneko.neko.medibox.data

import icu.takeneko.neko.medibox.writeInt
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

class MediBox(val medicines: Map<Int, Medicine> = mutableMapOf(),
              val usages: List<MedicineUsage> = mutableListOf()) {

    fun encode(): ByteArray {
        return encode(this)
    }

    companion object : BinaryData<MediBox> {
        override fun encode(obj: MediBox): ByteArray {
            val os = ByteArrayOutputStream()
            os.writeInt(obj.medicines.size)
            obj.medicines.values.forEach {
                os.write(Medicine.encode(it))
            }
            os.writeInt(obj.usages.size)
            obj.usages.forEach{
                os.write(MedicineUsage.encode(it))
            }
            return os.toByteArray()
        }

        override fun decode(byteBuffer: ByteBuffer): MediBox {
            val medicines = mutableListOf<Medicine>()
            val usages = mutableListOf<MedicineUsage>()
            val medicineCount = byteBuffer.getInt()
            repeat(medicineCount){
                medicines += Medicine.decode(byteBuffer)
            }
            val usageCount = byteBuffer.getInt()
            repeat(usageCount){
                usages += MedicineUsage.decode(byteBuffer)
            }
            checkMatching(medicines, usages)
            return MediBox(medicines.associateBy { it.medicineId }, usages)
        }

        private fun checkMatching(medicines:MutableList<Medicine>, usages: MutableList<MedicineUsage>){

        }
    }


}