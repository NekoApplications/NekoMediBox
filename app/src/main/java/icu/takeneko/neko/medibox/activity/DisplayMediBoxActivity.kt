package icu.takeneko.neko.medibox.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import icu.takeneko.neko.medibox.currentMediBox
import icu.takeneko.neko.medibox.data.MediBox
import icu.takeneko.neko.medibox.data.Medicine
import icu.takeneko.neko.medibox.data.MedicineUsage
import icu.takeneko.neko.medibox.data.OrderDuration
import icu.takeneko.neko.medibox.data.OrderDurationExactTime
import icu.takeneko.neko.medibox.data.UsageType
import icu.takeneko.neko.medibox.databinding.ActivityDisplayMediBoxBinding
import icu.takeneko.neko.medibox.testMediBox
import icu.takeneko.neko.medibox.util.AppActivity
import icu.takeneko.neko.medibox.util.sortByExactOrderDuration
import icu.takeneko.neko.medibox.view.MedicineUsageItemView
import kotlin.properties.Delegates

class DisplayMediBoxActivity : AppActivity() {

    private lateinit var binding: ActivityDisplayMediBoxBinding
    private var isPreview by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isPreview = intent.getBooleanExtra("preview", false)
        binding = ActivityDisplayMediBoxBinding.inflate(layoutInflater)
        currentMediBox = testMediBox
        binding.medList.apply {
            currentMediBox!!.usages.sortByExactOrderDuration().forEach {
                this.addView(MedicineUsageItemView(context).apply { this.medicineUsage = it })
            }
        }
        setSupportActionBar(binding.toolbar)
        binding.toolbar.apply {
            setNavigationOnClickListener {
                this@DisplayMediBoxActivity.finish()
            }
        }
        setContentView(binding.main)
    }
}