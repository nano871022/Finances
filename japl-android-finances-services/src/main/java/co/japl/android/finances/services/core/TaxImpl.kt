package co.japl.android.finances.services.core

import co.japl.android.finances.services.core.mapper.TaxMapper
import co.japl.android.finances.services.interfaces.ITaxSvc
import co.com.japl.finances.iports.outbounds.ITaxPort
import co.com.japl.finances.iports.dtos.TaxDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import javax.inject.Inject

class TaxImpl @Inject constructor(private val  taxSvc:ITaxSvc): ITaxPort {
    override fun get(codCreditCard: Int, month: Int, year: Int, kind: KindInterestRateEnum): TaxDTO? {
        val tax = taxSvc.get(codCreditCard.toLong(),month,year,co.japl.android.finances.services.enums.TaxEnum.findByOrdinal(kind.ordinal.toShort()))
        if(tax.isPresent){
            return TaxMapper.mapper(tax.get())
        }
        return null
    }
}