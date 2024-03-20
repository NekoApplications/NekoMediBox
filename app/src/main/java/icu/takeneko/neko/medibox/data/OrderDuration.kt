package icu.takeneko.neko.medibox.data

import androidx.annotation.StringRes
import icu.takeneko.neko.medibox.R

enum class OrderDuration(val countInSingleDay: Int, val descriptorStringRes: Int = 0) :
    Identifiable<OrderDuration> {
    PRN(0, R.string.text_duration_as_necessary),//as necessary
    QD(1, R.string.text_once_per_day),
    BID(2,R.string.text_twice_per_day),
    TID(3, R.string.text_third_times_per_day),
    QID(4, R.string.text_quad_times_per_day),
    QH(24, R.string.text_every_hour),//every hour
    Q2H(12, R.string.text_every_2_hour);//every 2 hour

    override fun id() = this.ordinal
    override fun match(id: Int): OrderDuration {
        for (t in OrderDuration.entries) {
            if (id == t.id()) return t
        }
        throw Identifiable.NotIdentifiableIdException(id, this)
    }
}

enum class OrderDurationExactTime(@StringRes val descriptorStringRes: Int = 0) :
    Identifiable<OrderDurationExactTime> {
    NONE(R.string.text_none),
    AC(R.string.text_exact_time_before_meals),
    PC(R.string.text_exact_time_after_meals),
    AM(R.string.text_exact_time_morning),
    PM(R.string.text_exact_time_afternoon),
    QN(R.string.text_exact_time_every_night),
    HS(R.string.text_exact_time_before_sleep);

    override fun id() = this.ordinal
    override fun match(id: Int): OrderDurationExactTime {
        for (t in OrderDurationExactTime.entries) {
            if (id == t.id()) return t
        }
        throw Identifiable.NotIdentifiableIdException(id, this)
    }
}