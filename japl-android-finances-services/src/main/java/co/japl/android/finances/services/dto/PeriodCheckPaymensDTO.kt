package co.japl.android.finances.services.pojo

data class PeriodCheckPaymentsPOJO (
        val period:String,
        val paid:Double,
        val amount:Double,
        val count:Long
    )