package co.com.japl.finances.iports.inbounds.creditcard.bought

import co.com.japl.finances.iports.enums.KindInterestRateEnum
import java.time.LocalDateTime

interface IBoughtSmsPort {

    fun createBySms(name:String,value:Double,date:LocalDateTime,codeCreditRate: Int,kind: KindInterestRateEnum)
}