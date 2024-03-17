package icu.takeneko.neko.medibox.data

import androidx.annotation.StringRes

enum class OrderDuration(val countInSingleDay: Int, @StringRes descriptorStringRes: Int = 0) :
    Identifiable<OrderDuration> {
    PRN(0),//as necessary
    QD(1),
    BID(2),
    TID(3),
    QID(4),
    QH(24),//every hour
    Q2H(12);//every 2 hour

    override fun id() = this.ordinal
    override fun match(id: Int): OrderDuration {
        for (t in OrderDuration.entries) {
            if (id == t.id()) return t
        }
        throw Identifiable.NotIdentifiableIdException(id, this)
    }
}

enum class OrderDurationExactTime(@StringRes descriptorStringRes: Int = 0) :
    Identifiable<OrderDurationExactTime> {
    NONE,
    AC,//before meals
    PC,//after meals
    AM,//morning
    PM,//afternoon
    QN,//every night
    HS;//before sleep

    override fun id() = this.ordinal
    override fun match(id: Int): OrderDurationExactTime {
        for (t in OrderDurationExactTime.entries) {
            if (id == t.id()) return t
        }
        throw Identifiable.NotIdentifiableIdException(id, this)
    }
}