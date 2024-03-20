package icu.takeneko.neko.medibox.activity

import android.os.Bundle
import android.view.View
import icu.takeneko.neko.medibox.currentMediBox
import icu.takeneko.neko.medibox.databinding.ActivityDisplayMediBoxBinding
import icu.takeneko.neko.medibox.testMediBox
import icu.takeneko.neko.medibox.util.AppActivity
import icu.takeneko.neko.medibox.util.sortByExactOrderDuration
import icu.takeneko.neko.medibox.view.MedicineUsageView
import kotlin.properties.Delegates

class DisplayMediBoxActivity : AppActivity() {

    private lateinit var binding: ActivityDisplayMediBoxBinding
    private var isPreview by Delegates.notNull<Boolean>()
    var onNextStepButtonPressedCallback: (DisplayMediBoxActivity) -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isPreview = intent.getBooleanExtra("preview", false)
        binding = ActivityDisplayMediBoxBinding.inflate(layoutInflater)
        binding.medList.apply {
            currentMediBox!!.usages.sortByExactOrderDuration().forEach {
                this.addView(MedicineUsageView(context).apply {
                    this.medicineUsage = it
                    isPreview = true
                })
            }
        }
        setSupportActionBar(binding.toolbar)
        binding.toolbar.apply {
            setNavigationOnClickListener {
                this@DisplayMediBoxActivity.finish()
            }
        }
        binding.buttonNextStep.visibility = if (isPreview) View.VISIBLE else View.GONE
        binding.buttonNextStep.setOnClickListener {
            onNextStepButtonPressedCallback(this)
        }
        setContentView(binding.main)
    }
}