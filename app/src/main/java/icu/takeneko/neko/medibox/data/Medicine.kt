package icu.takeneko.neko.medibox.data

import icu.takeneko.neko.medibox.getString
import icu.takeneko.neko.medibox.writeInt
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

class Medicine(val medicineId:Int, val name:String) {

    companion object : BinaryData<Medicine> {
        override fun encode(obj: Medicine): ByteArray {
            val os = ByteArrayOutputStream()
            os.writeInt(obj.medicineId)
            val arr = obj.name.encodeToByteArray()
            os.writeInt(arr.size)
            os.write(arr)
            return os.toByteArray()
        }

        override fun decode(byteBuffer: ByteBuffer): Medicine {
            val medicineId:Int = byteBuffer.getInt()
            val name = byteBuffer.getString()
            return Medicine(medicineId, name)
        }
    }
}