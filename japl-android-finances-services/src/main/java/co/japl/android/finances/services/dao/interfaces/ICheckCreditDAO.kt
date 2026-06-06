package co.japl.android.finances.services.dao.interfaces

import co.com.japl.finances.iports.dtos.CheckPaymentDTO
import co.japl.android.finances.services.dto.CheckCreditDTO
import co.japl.android.finances.services.interfaces.ISaveSvc
import co.japl.android.finances.services.interfaces.SaveSvc
import co.japl.android.finances.services.pojo.PeriodCheckPaymentsPOJO
import java.time.YearMonth
import java.util.Optional

interface ICheckCreditDAO: SaveSvc<CheckCreditDTO>, ISaveSvc<CheckCreditDTO> {
    fun getCheckPayment(codPaid:Int,period:String):Optional<CheckCreditDTO>
    fun getPeriodsPayment():List<PeriodCheckPaymentsPOJO>

}