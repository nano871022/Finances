package co.com.japl.module.paid.views.fakeSvc

import co.com.japl.finances.iports.dtos.ProjectionDTO
import co.com.japl.finances.iports.enums.KindPaymentsEnums
import co.com.japl.finances.iports.inbounds.paid.IProjectionFormPort
import java.math.BigDecimal
import java.time.LocalDate

class ProjectionFormPortFake : IProjectionFormPort {
    override fun save(projection: ProjectionDTO): Boolean {
        return true
    }

    override fun update(projection: ProjectionDTO): Boolean {
        return true
    }

    override fun findById(id: Int): ProjectionDTO? {
        return null
    }

    override fun calculateQuote(period: KindPaymentsEnums, date: LocalDate, value: BigDecimal): BigDecimal {
        return BigDecimal.ZERO
    }
}
