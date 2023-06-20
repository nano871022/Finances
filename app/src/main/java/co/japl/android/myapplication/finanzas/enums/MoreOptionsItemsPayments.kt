package co.japl.android.myapplication.finanzas.enums

enum class MoreOptionsItemsPayments(val i: Int) {

    DELETE(2),ENDING(0),UPDATE_VALUE(1);

    companion object {
        fun finByPosition(i: Int) = when (i) {
            0 -> MoreOptionsItemsPayments.ENDING
            1 -> MoreOptionsItemsPayments.UPDATE_VALUE
            2 -> MoreOptionsItemsPayments.DELETE
            else -> throw IllegalArgumentException("invalid value $i")
        }
    }

}