package co.japl.android.finances.services.enums

enum class MoreOptionsItemsPayments(val i: Int) {

    DELETE(2),ENDING(0),UPDATE_VALUE(1);

    companion object {
        fun finByPosition(i: Int) = when (i) {
            0 -> ENDING
            1 -> UPDATE_VALUE
            2 -> DELETE
            else -> throw IllegalArgumentException("invalid value $i")
        }
    }

}