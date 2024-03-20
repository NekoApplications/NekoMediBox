package icu.takeneko.neko.medibox.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.window.OnBackInvokedDispatcher
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import icu.takeneko.neko.medibox.R
import icu.takeneko.neko.medibox.currentMediBox
import icu.takeneko.neko.medibox.data.MediBox
import icu.takeneko.neko.medibox.data.Medicine
import icu.takeneko.neko.medibox.databinding.ActivityEditMedicinesBinding
import icu.takeneko.neko.medibox.util.AppActivity
import icu.takeneko.neko.medibox.util.format
import icu.takeneko.neko.medibox.util.randomIntId
import icu.takeneko.neko.medibox.view.MedicineView

class EditMedicinesActivity : AppActivity() {

    private lateinit var binding: ActivityEditMedicinesBinding
    private val medicines = mutableListOf<Medicine>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditMedicinesBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            if (medicines.isNotEmpty()) {
                showQuestionDialog(
                    R.string.text_confirm_exit,
                    negativeButtonText = R.string.no,
                    positiveButtonText = R.string.yes
                ) { _, _ ->
                    finish()
                }
            }else{
                finish()
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(OnBackInvokedDispatcher.PRIORITY_OVERLAY) {
                if (medicines.isNotEmpty()) {
                    showQuestionDialog(
                        R.string.text_confirm_exit,
                        negativeButtonText = R.string.no,
                        positiveButtonText = R.string.yes
                    ) { _, _ ->
                        finish()
                    }
                }
            }
        }
        medicines.addAll(currentMediBox?.medicines?.values ?: emptyList())
        binding.buttonAddMedicine.setOnClickListener {
            val textView = TextInputEditText(this).apply {
                setLayoutParams(
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                )
                isSingleLine = true
            }
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.text_add_medicine)
                .setView(LinearLayout(this).apply {
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
                    val name = textView.text?.toString() ?: ""
                    if (name.isEmpty()) {
                        return@setPositiveButton
                    }
                    medicines += Medicine(randomIntId(), name)
                    currentMediBox = MediBox(
                        medicines.associateBy { it.medicineId },
                        currentMediBox?.usages ?: emptyList()
                    )
                    updateMedicineList()
                }
                .setNegativeButton(R.string.label_cancel) { _, _ -> }
                .setCancelable(true)
                .show()
            textView.requestFocus()
        }
        binding.buttonNextStep.setOnClickListener {
            if (medicines.isEmpty()) {
                ToastUtils.showLong("¯\\\\_(ツ)_/¯")
                return@setOnClickListener
            }
            currentMediBox = MediBox(
                medicines.associateBy { it.medicineId },
                currentMediBox?.usages ?: emptyList()
            )
            ActivityUtils.startActivity(EditMedicineUsagesActivity::class.java)
        }
        updateMedicineList()
        setContentView(binding.main)
    }

    private fun updateMedicineList() {
        binding.medicineList.removeAllViews()
        binding.textNothing.visibility =
            if (medicines.isEmpty()) TextView.VISIBLE else TextView.GONE
        medicines.forEach {
            binding.medicineList.addView(MedicineView(this).apply {
                this.medicine = it
                setOnMedicineViewRemovedCallback {
                    showQuestionDialog(
                        format(R.string.text_confirm_remove_medicine, it.medicine.name),
                        negativeButtonText = R.string.no,
                        positiveButtonText = R.string.yes
                    ) { _,_ ->
                        this@EditMedicinesActivity.medicines -= medicine
                        updateMedicineList()
                    }
                }
            })
        }
    }


    @SuppressLint("MissingSuperCall")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (medicines.isNotEmpty()){
            showQuestionDialog(
                R.string.text_confirm_exit,
                negativeButtonText = R.string.no,
                positiveButtonText = R.string.yes
            ) { _, _ ->
                finish()
            }
        }else{
            super.onBackPressed()
        }
    }
}