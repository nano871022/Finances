package co.com.japl.finances.iports.inbounds.paid

import co.com.japl.finances.iports.enums.KindInterestRateEnum
import java.time.LocalDateTime

interface ISmsPort {

    fun createBySms(name:String,value:Double,date:LocalDateTime,codeAccount:Int)
}