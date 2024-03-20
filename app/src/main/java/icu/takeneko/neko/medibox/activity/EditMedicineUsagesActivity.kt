package icu.takeneko.neko.medibox.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import android.window.OnBackInvokedDispatcher
import com.blankj.utilcode.util.ActivityUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import icu.takeneko.neko.medibox.R
import icu.takeneko.neko.medibox.currentMediBox
import icu.takeneko.neko.medibox.data.MediBox
import icu.takeneko.neko.medibox.data.MedicineUsage
import icu.takeneko.neko.medibox.databinding.ActivityEditMedicineUsagesBinding
import icu.takeneko.neko.medibox.fragment.CreateMedicineUsageBottomSheetFragment
import icu.takeneko.neko.medibox.util.AppActivity
import icu.takeneko.neko.medibox.util.format
import icu.takeneko.neko.medibox.util.logI
import icu.takeneko.neko.medibox.view.MedicineUsageView

class EditMedicineUsagesActivity : AppActivity() {

    private lateinit var binding: ActivityEditMedicineUsagesBinding
    private val usages = mutableListOf<MedicineUsage>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityEditMedicineUsagesBinding.inflate(layoutInflater)
        binding.toolbar.setNavigationOnClickListener {
            if (usages.isNotEmpty()) {
                showQuestionDialog(
                    R.string.text_confirm_exit,
                    negativeButtonText = R.string.no,
                    positiveButtonText = R.string.yes
                ) { _, _ ->
                    finish()
                }
            } else {
                finish()
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(OnBackInvokedDispatcher.PRIORITY_OVERLAY) {
                if (usages.isNotEmpty()) {
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
        }
        binding.buttonAddMedicineUsage.setOnClickListener {
            CreateMedicineUsageBottomSheetFragment().apply {
                show(supportFragmentManager, "BottomSheet")
                onDoneButtonClickedCallback = {
                    usages.add(it)
                    updateUsageList()
                }
            }
        }
        binding.buttonNextStep.setOnClickListener {
            ActivityUtils.startActivity(WriteTagActivity::class.java)
        }
        usages.addAll(currentMediBox?.usages ?: emptyList())
        updateUsageList()
        setContentView(binding.main)
    }

    private fun updateUsageList() {
        currentMediBox = MediBox(currentMediBox!!.medicines, usages)
        val ids = currentMediBox!!.medicines.keys
        usages.removeIf { it.medicineId !in ids }
        binding.medicineUsagesList.removeAllViews()
        binding.textNothing.visibility =
            if (usages.isEmpty()) TextView.VISIBLE else TextView.GONE

        usages.forEach {
            binding.medicineUsagesList.addView(MedicineUsageView(this).apply {
                this.medicineUsage = it
                setOnUsageRemovedCallback {
                    showQuestionDialog(
                        format(R.string.text_confirm_remove_medicine_usage),
                        negativeButtonText = R.string.no,
                        positiveButtonText = R.string.yes
                    ) { _, _ ->
                        this@EditMedicineUsagesActivity.usages -= medicineUsage
                        updateUsageList()
                    }
                }
                setOnClickListener {
                    val oldUsage = medicineUsage
                    CreateMedicineUsageBottomSheetFragment().apply {
                        show(supportFragmentManager, "BottomSheet")
                        oldMedicineUsage = oldUsage
                        onDoneButtonClickedCallback = {
                            usages.remove(oldUsage)
                            usages.add(it)
                            updateUsageList()
                        }
                    }
                }
            })
        }
    }

    @SuppressLint("MissingSuperCall")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (usages.isNotEmpty()){
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