package icu.takeneko.neko.medibox.data

enum class UsageType : Identifiable<UsageType> {
    MILLIGRAM, PILL, HALF_PILL;

    override fun id() = this.ordinal
    override fun match(id:Int): UsageType {
        for (t in UsageType.entries){
            if (id == t.id())return t
        }
        throw Identifiable.NotIdentifiableIdException(id, this)
    }
}