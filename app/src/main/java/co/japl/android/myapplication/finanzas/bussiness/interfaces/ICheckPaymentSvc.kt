package co.japl.android.myapplication.finanzas.bussiness.interfaces

import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.CheckPaymentsDTO
import co.japl.android.myapplication.finanzas.pojo.PeriodCheckPaymentsPOJO
import java.util.Optional

interface ICheckPaymentSvc: SaveSvc<CheckPaymentsDTO>,ISaveSvc<CheckPaymentsDTO> {
    fun getCheckPayment(codPaid:Int,period:String):Optional<CheckPaymentsDTO>
    fun getPeriodsPayment():List<PeriodCheckPaymentsPOJO>
}