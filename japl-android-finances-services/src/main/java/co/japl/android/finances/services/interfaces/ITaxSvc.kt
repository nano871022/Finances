package co.japl.android.finances.services.interfaces

import co.japl.android.finances.services.dto.TaxDTO
import co.japl.android.finances.services.enums.TaxEnum
import java.util.*

interface ITaxSvc : SaveSvc<TaxDTO>{
    fun get(codCreditCard:Long,month:Int, year:Int, kind: TaxEnum = TaxEnum.CREDIT_CARD): Optional<TaxDTO>
}