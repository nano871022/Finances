package co.com.japl.finances.iports.inbounds.creditcard.bought

import co.com.japl.finances.iports.enums.KindInterestRateEnum

interface IBoughtSmsPort {

    fun createBySms(name:String,value:String,date:String,codeCreditRate: Int,kind: KindInterestRateEnum)
}