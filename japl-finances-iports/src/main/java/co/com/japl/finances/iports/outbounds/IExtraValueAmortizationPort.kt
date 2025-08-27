package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.ExtraValueAmortizationDTO

interface IExtraValueAmortizationPort {

    fun getByCode(code:Int):List<ExtraValueAmortizationDTO>

    fun save(dto: ExtraValueAmortizationDTO):Long
}