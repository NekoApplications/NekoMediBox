package icu.takeneko.neko.medibox.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textview.MaterialTextView
import icu.takeneko.neko.medibox.R
import icu.takeneko.neko.medibox.currentMediBox
import icu.takeneko.neko.medibox.data.MedicineUsage
import icu.takeneko.neko.medibox.util.format
import kotlin.properties.Delegates

class MedicineUsageView : LinearLayout {
    private lateinit var _medicineUsage: MedicineUsage
    private val textMedicineName: MaterialTextView
    private val textMedicineUsage: MaterialTextView
    private lateinit var buttonRemove: FloatingActionButton
    var isPreview by Delegates.observable(false) { _, _, after ->
        buttonRemove.visibility = if (after) View.GONE else View.VISIBLE
    }
    private var onUsageRemovedCallback: ((MedicineUsageView) -> Unit)? = null
    var medicineUsage: MedicineUsage
        set(value) {
            _medicineUsage = value
            textMedicineName.text = currentMediBox?.getMedicineById(value.medicineId)?.name
                ?: context.format(R.string.text_unknown_medicine, value.medicineId)
            textMedicineUsage.text = value.describeUsage(context)
        }
        get() {
            return _medicineUsage
        }

    fun setOnUsageRemovedCallback(cb: (MedicineUsageView) -> Unit){
        onUsageRemovedCallback = cb
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        findViewById<MaterialCardView>(R.id.usage_card).setOnClickListener(l)
    }

    constructor(context: Context) : super(context) {
        LayoutInflater.from(context).inflate(R.layout.medicine_usage_view, this)
        textMedicineUsage = findViewById(R.id.text_medicine_usage)
        textMedicineName = findViewById(R.id.text_medicine_name)
        buttonRemove = findViewById(R.id.button_remove_usage)
        buttonRemove.setOnClickListener { onUsageRemovedCallback?.invoke(this) }
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        LayoutInflater.from(context).inflate(R.layout.medicine_usage_view, this)
        textMedicineUsage = findViewById(R.id.text_medicine_usage)
        textMedicineName = findViewById(R.id.text_medicine_name)
        buttonRemove = findViewById(R.id.button_remove_usage)
        buttonRemove.setOnClickListener { onUsageRemovedCallback?.invoke(this) }
    }
}