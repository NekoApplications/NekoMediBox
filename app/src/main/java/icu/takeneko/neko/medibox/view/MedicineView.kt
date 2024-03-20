package icu.takeneko.neko.medibox.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import icu.takeneko.neko.medibox.R
import icu.takeneko.neko.medibox.currentMediBox
import icu.takeneko.neko.medibox.data.Medicine
import icu.takeneko.neko.medibox.data.MedicineUsage
import icu.takeneko.neko.medibox.databinding.MedicineUsageViewBinding
import icu.takeneko.neko.medibox.util.format

class MedicineView : LinearLayout {
    private lateinit var _medicine: Medicine
    private lateinit var textMedicineName: MaterialTextView
    private lateinit var card: MaterialCardView
    private lateinit var removeButton:FloatingActionButton
    private var onMedicineViewRemovedCallback: (MedicineView) -> Unit = {}
    var medicine: Medicine
        set(value) {
            _medicine = value
            textMedicineName.text = _medicine.name
        }
        get() {
            return _medicine
        }

    constructor(context: Context) : super(context) {
        LayoutInflater.from(context).inflate(R.layout.medicine_view, this)
        initViews()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        LayoutInflater.from(context).inflate(R.layout.medicine_view, this)
        initViews()
    }

    fun setOnMedicineViewRemovedCallback(callback: (MedicineView) -> Unit){
        onMedicineViewRemovedCallback = callback
    }

    private fun initViews() {
        textMedicineName = findViewById(R.id.text_name)
        card = findViewById(R.id.medicine_card)
        removeButton = findViewById(R.id.button_remove_medicine)
        card.setOnClickListener {
            val textView = TextInputEditText(context).apply {
                setLayoutParams(
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                )
                isSingleLine = true
            }
            textView.setText(medicine.name)
            MaterialAlertDialogBuilder(context)
                .setTitle(R.string.text_rename)
                .setView(LinearLayout(context).apply {
                    setPadding(10.dp, 14.dp, 10.dp, 24.dp)
                    setLayoutParams(
                        ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    )
                    gravity = Gravity.CENTER
                    addView(textView)
                })
                .setPositiveButton(R.string.label_ok) { _, _ ->
                    val newName = textView.text?.toString() ?: ""
                    if (newName.isEmpty()){
                        ToastUtils.showLong(R.string.text_empty_medicine_name)
                        return@setPositiveButton
                    }
                    _medicine.name = newName
                    textMedicineName.text = newName
                }
                .setNegativeButton(R.string.label_cancel) { _, _ -> }
                .setCancelable(true)
                .show()
        }
        removeButton.setOnClickListener {
            onMedicineViewRemovedCallback(this)
        }
    }

    private val Int.dp
        get() = (resources.displayMetrics.density * this + 0.5f).toInt()
}