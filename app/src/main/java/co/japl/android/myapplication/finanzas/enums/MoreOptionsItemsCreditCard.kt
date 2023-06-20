package co.japl.android.myapplication.finanzas.enums

import android.util.Log

enum class MoreOptionsItemsCreditCard(val i: Int) {

    EDIT(3),AMORTIZATION(0),DELETE(4),ENDING(1),UPDATE_VALUE(2);

    companion object{
        fun findByOrdinal(i: Int): MoreOptionsItemsCreditCard {
            return when(i){
                0-> AMORTIZATION
                1-> ENDING
                2-> UPDATE_VALUE
                3-> EDIT
                4-> DELETE
                else -> throw IllegalArgumentException("Invalid Option $i")
            }
    }
        }
}