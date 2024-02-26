package co.japl.android.finances.services.enums

import android.util.Log

enum class MoreOptionsItemsCreditCard(val i: Int) {

    EDIT(4),AMORTIZATION(0),DELETE(5),ENDING(1),UPDATE_VALUE(2),DIFFER_INSTALLMENT(3);

    companion object{
        fun findByOrdinal(i: Int): MoreOptionsItemsCreditCard {
            return when(i){
                0-> AMORTIZATION
                1-> ENDING
                2-> UPDATE_VALUE
                3-> DIFFER_INSTALLMENT
                4-> EDIT
                5-> DELETE
                else -> throw IllegalArgumentException("Invalid Option $i")
            }
    }
        }
}