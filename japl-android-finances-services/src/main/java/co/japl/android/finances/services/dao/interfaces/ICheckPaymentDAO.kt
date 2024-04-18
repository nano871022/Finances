package co.japl.android.finances.services.dao.interfaces

import co.japl.android.finances.services.dto.CheckPaymentsDTO
import co.japl.android.finances.services.interfaces.ISaveSvc
import co.japl.android.finances.services.interfaces.SaveSvc
import co.japl.android.finances.services.pojo.PeriodCheckPaymentsPOJO
import java.util.Optional

interface ICheckPaymentDAO: SaveSvc<CheckPaymentsDTO>, ISaveSvc<CheckPaymentsDTO> {
    fun getCheckPayment(codPaid:Int,period:String):Optional<CheckPaymentsDTO>
    fun getPeriodsPayment():List<PeriodCheckPaymentsPOJO>
}