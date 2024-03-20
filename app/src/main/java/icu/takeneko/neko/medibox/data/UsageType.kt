package icu.takeneko.neko.medibox.data

import icu.takeneko.neko.medibox.R

enum class UsageType(val resId: Int, val descriptorStringRes: Int) : Identifiable<UsageType> {
    MILLIGRAM(R.string.text_mg, R.string.text_desc_mg),
    PILL(R.string.text_one_pill, R.string.text_one_pill),
    HALF_PILL(R.string.text_half_pill, R.string.text_half_pill);

    override fun id() = this.ordinal
    override fun match(id: Int): UsageType {
        for (t in UsageType.entries) {
            if (id == t.id()) return t
        }
        throw Identifiable.NotIdentifiableIdException(id, this)
    }
}