package co.com.japl.module.paid.views.periods.list.fakes

import co.com.japl.finances.iports.dtos.PeriodPaidDTO
import co.com.japl.finances.iports.inbounds.paid.IPeriodPaidPort
import java.math.BigDecimal
import java.time.YearMonth

class FakePeriodPaidPort : IPeriodPaidPort {
    override fun get(codeAccount: Int): List<PeriodPaidDTO> {
        return listOf(
            PeriodPaidDTO(
                date = YearMonth.now(),
                value = BigDecimal(1000)
            ),
            PeriodPaidDTO(
                date = YearMonth.now().minusMonths(1),
                value = BigDecimal(2000)
            )
        )
    }
}
