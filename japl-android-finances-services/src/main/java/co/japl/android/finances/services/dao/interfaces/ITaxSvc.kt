package co.japl.android.finances.services.dao.interfaces

import co.japl.android.finances.services.dto.TaxDTO
import co.japl.android.finances.services.enums.TaxEnum
import co.japl.android.finances.services.interfaces.SaveSvc
import java.util.*

interface ITaxSvc : SaveSvc<TaxDTO> {
    fun get(codCreditCard:Long,month:Int, year:Int, kind: TaxEnum = TaxEnum.CREDIT_CARD): Optional<TaxDTO>
}