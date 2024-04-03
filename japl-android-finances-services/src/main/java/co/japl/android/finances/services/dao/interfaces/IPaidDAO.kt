package co.japl.android.finances.services.dao.interfaces

import co.japl.android.finances.services.dto.PaidDTO
import co.japl.android.finances.services.interfaces.ISaveSvc
import co.japl.android.finances.services.interfaces.SaveSvc
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth

interface IPaidDAO: SaveSvc<PaidDTO>, ISaveSvc<PaidDTO> {

    fun getTotalPaid(current:LocalDate = LocalDate.now()):BigDecimal
    fun getRecurrent(date: LocalDate):List<PaidDTO>

    fun getAll(codeAccount:Int, period: YearMonth):List<PaidDTO>

    fun getRecurrents(codeAccount: Int, period: YearMonth): List<PaidDTO>

    fun getPeriods():List<PaidDTO>

    fun findByNameValueDate(values: PaidDTO): List<PaidDTO>
}