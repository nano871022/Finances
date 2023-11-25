package co.japl.finances.core.adapters.outbound.interfaces

import co.japl.finances.core.dto.TaxDTO
import co.japl.finances.core.enums.KindOfTaxEnum
import co.japl.finances.core.enums.TaxEnum

interface ITaxPort {

    fun get(codCreditCard:Int,month:Int, year:Int,kind: TaxEnum):TaxDTO?

}