package icu.takeneko.neko.medibox

import android.content.DialogInterface
import icu.takeneko.neko.medibox.data.MediBox
import icu.takeneko.neko.medibox.data.Medicine
import icu.takeneko.neko.medibox.data.MedicineUsage
import icu.takeneko.neko.medibox.data.OrderDuration
import icu.takeneko.neko.medibox.data.OrderDurationExactTime
import icu.takeneko.neko.medibox.data.UsageType

const val TAG = "NekoMediBox"

var currentMediBox: MediBox? = null

val defaultDialogButtonCallback: DialogInterface.OnClickListener =
    DialogInterface.OnClickListener { _, _ -> }

const val mediBoxVersion = 0x0004

lateinit var mainActivity: MainActivity

val testMediBox = MediBox(
    listOf(
        Medicine(1, "枸橼酸坦度螺酮片"),
        Medicine(2, "戊酸雌二醇片"),
        Medicine(4, "扎来普隆分散片"),
        Medicine(5, "厄贝沙坦片"),
        Medicine(6, "盐酸帕罗西汀片")
    ).associateBy { it.medicineId },
    listOf(
        MedicineUsage(
            4,
            UsageType.MILLIGRAM,
            5,
            OrderDuration.QD,
            OrderDurationExactTime.HS
        ),
        MedicineUsage(
            1,
            UsageType.MILLIGRAM,
            10,
            OrderDuration.TID,
            OrderDurationExactTime.PC
        ),
        MedicineUsage(
            6,
            UsageType.MILLIGRAM,
            20,
            OrderDuration.QD,
            OrderDurationExactTime.AM
        ),
        MedicineUsage(
            5,
            UsageType.MILLIGRAM,
            75,
            OrderDuration.QD,
            OrderDurationExactTime.AM
        ),
        MedicineUsage(
            2,
            UsageType.MILLIGRAM,
            2,
            OrderDuration.QD,
            OrderDurationExactTime.AM
        ),
        MedicineUsage(
            2,
            UsageType.MILLIGRAM,
            2,
            OrderDuration.QD,
            OrderDurationExactTime.PM
        )
    )
)