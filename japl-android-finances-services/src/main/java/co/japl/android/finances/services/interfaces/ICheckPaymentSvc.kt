package co.japl.android.finances.services.interfaces

import co.japl.android.finances.services.dto.CheckPaymentsDTO
import co.japl.android.finances.services.pojo.PeriodCheckPaymentsPOJO
import java.util.Optional

interface ICheckPaymentSvc: SaveSvc<CheckPaymentsDTO>,ISaveSvc<CheckPaymentsDTO> {
    fun getCheckPayment(codPaid:Int,period:String):Optional<CheckPaymentsDTO>
    fun getPeriodsPayment():List<PeriodCheckPaymentsPOJO>
}