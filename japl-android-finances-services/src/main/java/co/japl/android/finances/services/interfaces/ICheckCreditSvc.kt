package co.japl.android.finances.services.interfaces

import co.japl.android.finances.services.dto.CheckCreditDTO
import co.japl.android.finances.services.pojo.PeriodCheckPaymentsPOJO
import java.util.Optional

interface ICheckCreditSvc: SaveSvc<CheckCreditDTO>,ISaveSvc<CheckCreditDTO> {
    fun getCheckPayment(codPaid:Int,period:String):Optional<CheckCreditDTO>
    fun getPeriodsPayment():List<PeriodCheckPaymentsPOJO>
}