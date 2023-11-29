package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.TaxDTO
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.enums.TaxEnum

interface ITaxPort {

    fun get(codCreditCard:Int,month:Int, year:Int,kind: TaxEnum):TaxDTO?

}