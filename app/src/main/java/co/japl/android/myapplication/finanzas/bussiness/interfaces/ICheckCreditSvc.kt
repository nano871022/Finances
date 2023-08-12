package co.japl.android.myapplication.finanzas.bussiness.interfaces

import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.CheckCreditDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.CheckPaymentsDTO
import co.japl.android.myapplication.finanzas.pojo.PeriodCheckPaymentsPOJO
import java.util.Optional

interface ICheckCreditSvc: SaveSvc<CheckCreditDTO>,ISaveSvc<CheckCreditDTO> {
    fun getCheckPayment(codPaid:Int,period:String):Optional<CheckCreditDTO>
    fun getPeriodsPayment():List<PeriodCheckPaymentsPOJO>
}