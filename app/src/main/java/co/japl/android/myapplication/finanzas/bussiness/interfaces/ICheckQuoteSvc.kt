package co.japl.android.myapplication.finanzas.bussiness.interfaces

import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.CheckPaymentsDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.CheckQuoteDTO
import co.japl.android.myapplication.finanzas.pojo.PeriodCheckPaymentsPOJO
import java.util.Optional

interface ICheckQuoteSvc: SaveSvc<CheckQuoteDTO>,ISaveSvc<CheckQuoteDTO> {
    fun getCheckPayment(codPaid:Int,period:String):Optional<CheckQuoteDTO>
    fun getPeriodsPayment():List<PeriodCheckPaymentsPOJO>
}