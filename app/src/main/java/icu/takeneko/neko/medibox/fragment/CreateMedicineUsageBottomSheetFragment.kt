package icu.takeneko.neko.medibox.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginBottom
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.bottomsheet.BottomSheetDragHandleView
import icu.takeneko.neko.medibox.data.MedicineUsage
import icu.takeneko.neko.medibox.view.CreateMedicineDialogView
import icu.takeneko.neko.medibox.view.MedicineUsageView

class CreateMedicineUsageBottomSheetFragment : BottomSheetDialogFragment() {

    var onDoneButtonClickedCallback: (MedicineUsage) -> Unit = {}
    var oldMedicineUsage:MedicineUsage? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext()).apply {
            dismissWithAnimation = true
            setContentView(CreateMedicineDialogView(requireContext()).apply {
                onButtonClickedCallback = {
                    this@CreateMedicineUsageBottomSheetFragment.dismiss()
                    onDoneButtonClickedCallback(it)
                }
                this.oldMedicineUsage = this@CreateMedicineUsageBottomSheetFragment.oldMedicineUsage
            })
        }

}