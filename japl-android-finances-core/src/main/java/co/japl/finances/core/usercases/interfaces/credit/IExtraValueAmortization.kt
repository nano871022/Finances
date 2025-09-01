package co.japl.finances.core.usercases.interfaces.credit

import co.com.japl.finances.iports.dtos.ExtraValueAmortizationDTO

interface IExtraValueAmortization {

    fun getByCodeCredit(id:Int):List<ExtraValueAmortizationDTO>

    fun save(id:Int,numQuotes:Long,value:Double):Int
}