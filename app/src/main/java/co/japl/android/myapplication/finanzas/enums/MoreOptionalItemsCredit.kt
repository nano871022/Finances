package co.japl.android.myapplication.finanzas.enums

enum class MoreOptionalItemsCredit(val i:Int) {
    AMORTIZATION(0), DELETE(3), GRACE_PERIOD(1), DELETE_GRACE_PERIOD(2);

    companion object{
        fun find(i:Int):MoreOptionalItemsCredit{
            return when(i){
                0->  AMORTIZATION
                1->  GRACE_PERIOD
                2->  DELETE_GRACE_PERIOD
                3-> DELETE
                else -> throw IllegalArgumentException("Invalid Option $i")
            }
        }
    }
}