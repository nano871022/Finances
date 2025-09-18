package co.com.japl.module.paid.views.projections.form.fakes

import co.com.japl.finances.iports.dtos.ProjectionDTO
import co.com.japl.finances.iports.enums.KindPaymentsEnums
import co.com.japl.finances.iports.inbounds.paid.IProjectionFormPort
import java.math.BigDecimal
import java.time.LocalDate

class FakeProjectionFormPort : IProjectionFormPort {
    override fun findById(id: Int): ProjectionDTO? {
        return ProjectionDTO(
            id = 1,
            create = LocalDate.now(),
            end = LocalDate.now().plusMonths(12),
            name = "Test Projection",
            value = BigDecimal(1000000),
            quote = BigDecimal(100000),
            monthsLeft = 10,
            amountSaved = BigDecimal(200000),
            type = KindPaymentsEnums.MONTHLY.name
        )
    }

    override fun save(projection: ProjectionDTO): Boolean {
        return true
    }

    override fun calculateQuote(
        kindOf: KindPaymentsEnums,
        endDate: LocalDate,
        value: BigDecimal
    ): BigDecimal {
        return value.divide(BigDecimal(kindOf.month), 2, BigDecimal.ROUND_HALF_UP)
    }
}
