package icu.takeneko.neko.medibox.data

import icu.takeneko.neko.medibox.R
import icu.takeneko.neko.medibox.util.InvalidMediBoxDataException
import icu.takeneko.neko.medibox.util.writeInt
import java.io.ByteArrayOutputStream
import java.lang.IndexOutOfBoundsException
import java.nio.BufferUnderflowException
import java.nio.ByteBuffer

class MediBox(
    val medicines: Map<Int, Medicine> = mutableMapOf(),
    val usages: List<MedicineUsage> = mutableListOf(),
    val iconEmojiChar: Char = Char.MIN_VALUE
) {

    fun encode(): ByteArray {
        return encode(this)
    }

    fun getMedicineById(id:Int) = medicines[id]

    companion object : BinaryData<MediBox> {
        override fun encode(obj: MediBox): ByteArray {
            val os = ByteArrayOutputStream()
            os.writeInt(obj.medicines.size)
            obj.medicines.values.forEach {
                os.write(Medicine.encode(it))
            }
            os.writeInt(obj.usages.size)
            obj.usages.forEach {
                os.write(MedicineUsage.encode(it))
            }
            return os.toByteArray()
        }

        override fun decode(byteBuffer: ByteBuffer): MediBox {
            val medicines = mutableListOf<Medicine>()
            val usages = mutableListOf<MedicineUsage>()
            val medicineCount = byteBuffer.getInt()
            var actualCount = 0
            try {
                repeat(medicineCount) {
                    actualCount = it
                    medicines += Medicine.decode(byteBuffer)
                }
                val usageCount = byteBuffer.getInt()
                repeat(usageCount) {
                    actualCount = it
                    usages += MedicineUsage.decode(byteBuffer)
                }
            } catch (e: BufferUnderflowException) {
                throw InvalidMediBoxDataException(R.string.text_data_length_mismatch)
            } catch (e: IndexOutOfBoundsException) {
                throw InvalidMediBoxDataException(R.string.text_data_length_mismatch)
            }
            checkMatching(medicines, usages)
            return MediBox(medicines.associateBy { it.medicineId }, usages)
        }

        private fun checkMatching(
            medicines: MutableList<Medicine>,
            usages: MutableList<MedicineUsage>
        ) {
            if (medicines.isEmpty() && usages.isEmpty()) {
                throw InvalidMediBoxDataException(R.string.text_empty_data)
            }
            val ids = medicines.map { it.medicineId }
            val idNotExists = mutableListOf<MedicineUsage>()
            for (u in usages) {
                if (u.medicineId !in ids) {
                    idNotExists.add(u)
                }
            }
            if (idNotExists.isNotEmpty()){
                throw InvalidMediBoxDataException(R.string.text_medicine_not_found, idNotExists)
            }
        }
    }


}