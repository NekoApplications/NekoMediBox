package icu.takeneko.neko.medibox.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.doOnTextChanged
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import icu.takeneko.neko.medibox.R
import icu.takeneko.neko.medibox.currentMediBox
import icu.takeneko.neko.medibox.data.Medicine
import icu.takeneko.neko.medibox.data.MedicineUsage
import icu.takeneko.neko.medibox.data.OrderDuration
import icu.takeneko.neko.medibox.data.OrderDurationExactTime
import icu.takeneko.neko.medibox.data.UsageType
import icu.takeneko.neko.medibox.util.format
import kotlin.properties.Delegates

class CreateMedicineDialogView : CoordinatorLayout {

    private val buttonDone: MaterialButton

    private val medicineSpinner: Spinner
    private val useTypeSpinner: Spinner
    private val orderDurationSpinner: Spinner
    private val exactOrderDurationSpinner: Spinner

    private val textAmount: TextInputEditText
    private val indexMap: MutableMap<Int, Medicine> = mutableMapOf()

    private lateinit var medicineUsage: MedicineUsage

    var onButtonClickedCallback: (MedicineUsage) -> Unit = {}
    var oldMedicineUsage: MedicineUsage? by Delegates.observable(null){_,_,v ->
        if (v != null) {
            applyOldUsage(v)
        }
    }

    constructor(context: Context) : super(context) {
        LayoutInflater.from(context).inflate(R.layout.create_medicine_dialog_view, this)
        buttonDone = findViewById(R.id.button_done)
        medicineSpinner = findViewById(R.id.medicine_spinner)
        useTypeSpinner = findViewById(R.id.spinner_use_type)
        textAmount = findViewById(R.id.text_amount)
        orderDurationSpinner = findViewById(R.id.spinner_order_duration)
        exactOrderDurationSpinner = findViewById(R.id.spinner_exact_order_duration)
        setListAdapter()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        LayoutInflater.from(context).inflate(R.layout.create_medicine_dialog_view, this)
        buttonDone = findViewById(R.id.button_done)
        medicineSpinner = findViewById(R.id.medicine_spinner)
        useTypeSpinner = findViewById(R.id.spinner_use_type)
        textAmount = findViewById(R.id.text_amount)
        orderDurationSpinner = findViewById(R.id.spinner_order_duration)
        exactOrderDurationSpinner = findViewById(R.id.spinner_exact_order_duration)
        setListAdapter()
    }

    private fun setListAdapter() {
        medicineSpinner.adapter = ArrayAdapter(context, R.layout.text_array_adapter, buildList {
            var index = 0
            currentMediBox?.medicines?.values?.forEach {
                add(it.name)
                indexMap[index++] = it
            }
        })
        medicineSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                medicineUsage = MedicineUsage(
                    indexMap[position]!!.medicineId,
                    medicineUsage.usageType,
                    tryParseAmountText(),
                    medicineUsage.orderDuration,
                    medicineUsage.orderDurationExactTime
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
        useTypeSpinner.adapter = ArrayAdapter(context, R.layout.text_array_adapter, buildList {
            UsageType.entries.forEach {
                add(context.format(it.descriptorStringRes))
            }
        })
        useTypeSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                medicineUsage = MedicineUsage(
                    medicineUsage.medicineId,
                    UsageType.entries[position],
                    tryParseAmountText(),
                    medicineUsage.orderDuration,
                    medicineUsage.orderDurationExactTime
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
        orderDurationSpinner.adapter =
            ArrayAdapter(context, R.layout.text_array_adapter, buildList {
                OrderDuration.entries.forEach {
                    add(context.format(it.descriptorStringRes))
                }
            })
        orderDurationSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                medicineUsage = MedicineUsage(
                    medicineUsage.medicineId,
                    medicineUsage.usageType,
                    tryParseAmountText(),
                    OrderDuration.entries[position],
                    medicineUsage.orderDurationExactTime
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
        exactOrderDurationSpinner.adapter =
            ArrayAdapter(context, R.layout.text_array_adapter, buildList {
                OrderDurationExactTime.entries.forEach {
                    add(context.format(it.descriptorStringRes))
                }
            })
        exactOrderDurationSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                medicineUsage = MedicineUsage(
                    medicineUsage.medicineId,
                    medicineUsage.usageType,
                    tryParseAmountText(),
                    medicineUsage.orderDuration,
                    OrderDurationExactTime.entries[position]
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        medicineUsage = MedicineUsage(
            currentMediBox!!.medicines.values.toList().component1().medicineId,
            UsageType.MILLIGRAM,
            0,
            OrderDuration.QD,
            OrderDurationExactTime.AM
        )
        textAmount.doOnTextChanged { _, _, _, _ ->
            medicineUsage = MedicineUsage(
                medicineUsage.medicineId,
                medicineUsage.usageType,
                tryParseAmountText(),
                medicineUsage.orderDuration,
                medicineUsage.orderDurationExactTime
            )
        }
        buttonDone.setOnClickListener {
            onButtonClickedCallback(medicineUsage)
        }
    }

    private fun applyOldUsage(usage: MedicineUsage){
        usage.apply {
            indexMap.forEach { (t, u) ->
                if (u.medicineId == this.medicineId) {
                    medicineSpinner.setSelection(t)
                }
            }
            useTypeSpinner.setSelection(usageType.ordinal)
            orderDurationSpinner.setSelection(orderDuration.ordinal)
            exactOrderDurationSpinner.setSelection(orderDurationExactTime.ordinal)
            textAmount.setText(count.toString())
            medicineUsage = MedicineUsage(
                medicineId,
                usageType,
                count,
                orderDuration,
                orderDurationExactTime
            )
        }
    }

    fun tryParseAmountText(): Int {
        val text = textAmount.text?.toString() ?: "0"
        return text.toIntOrNull() ?: 0
    }
}
