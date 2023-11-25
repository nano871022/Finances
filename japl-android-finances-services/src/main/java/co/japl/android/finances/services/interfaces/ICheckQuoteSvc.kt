package co.japl.android.finances.services.interfaces

import co.japl.android.finances.services.dto.CheckQuoteDTO
import co.japl.android.finances.services.pojo.PeriodCheckPaymentsPOJO
import java.util.Optional

interface ICheckQuoteSvc: SaveSvc<CheckQuoteDTO>,ISaveSvc<CheckQuoteDTO> {
    fun getCheckPayment(codPaid:Int,period:String):Optional<CheckQuoteDTO>
    fun getPeriodsPayment():List<PeriodCheckPaymentsPOJO>
}