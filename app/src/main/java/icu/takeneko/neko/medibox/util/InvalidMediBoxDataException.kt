package icu.takeneko.neko.medibox.util

import icu.takeneko.neko.medibox.data.MedicineUsage

class InvalidMediBoxDataException(
    val messageResId: Int,
    val missingMedicineList: List<MedicineUsage> = listOf(),
) : Exception() {
}