package co.japl.android.finances.services.enums

enum class MoreOptionalItemsCredit(val i:Int) {
    ADDITIONAL(0),AMORTIZATION(1), GRACE_PERIOD(2), DELETE_GRACE_PERIOD(3), DELETE(4);

    companion object{
        fun find(i:Int):MoreOptionalItemsCredit{
            return when(i){
                0 -> ADDITIONAL
                1->  AMORTIZATION
                2->  GRACE_PERIOD
                3->  DELETE_GRACE_PERIOD
                4-> DELETE
                else -> throw IllegalArgumentException("Invalid Option $i")
            }
        }
    }
}