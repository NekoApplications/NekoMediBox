package icu.takeneko.neko.medibox.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.google.android.material.textview.MaterialTextView
import icu.takeneko.neko.medibox.R
import icu.takeneko.neko.medibox.currentMediBox
import icu.takeneko.neko.medibox.data.MedicineUsage
import icu.takeneko.neko.medibox.databinding.MedicineUsageViewBinding
import icu.takeneko.neko.medibox.util.format

class MedicineUsageItemView:LinearLayout {
    private lateinit var _medicineUsage:MedicineUsage
    private val textMedicineName:MaterialTextView
    private val textMedicineUsage:MaterialTextView
    var medicineUsage:MedicineUsage
        set(value) {
            _medicineUsage = value
            textMedicineName.text = currentMediBox?.getMedicineById(value.medicineId)?.name
                ?: context.format(R.string.text_unknown_medicine, value.medicineId)
            textMedicineUsage.text = value.describeUsage(context)
        }
        get() {
            return _medicineUsage
        }

    constructor(context: Context) : super(context) {
        LayoutInflater.from(context).inflate(R.layout.medicine_usage_view, this)
        textMedicineUsage = findViewById(R.id.text_medicine_usage)
        textMedicineName = findViewById(R.id.text_medicine_name)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        LayoutInflater.from(context).inflate(R.layout.medicine_usage_view, this)
        textMedicineUsage = findViewById(R.id.text_medicine_usage)
        textMedicineName = findViewById(R.id.text_medicine_name)
    }
}