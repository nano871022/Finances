package co.com.japl.finances.iports.inbounds.credit

import co.com.japl.finances.iports.dtos.ExtraValueAmortizationDTO

interface IExtraValueAmortizationCreditPort {

    fun getAll(id:Int):List<ExtraValueAmortizationDTO>

    fun save(id:Int,numQuotes:Long,value:Double):Int
}