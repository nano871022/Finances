package co.com.japl.module.paid.views.fakeSvc

import co.com.japl.finances.iports.dtos.ProjectionDTO
import co.com.japl.finances.iports.enums.KindPaymentsEnums
import co.com.japl.finances.iports.inbounds.paid.IProjectionFormPort
import java.math.BigDecimal
import java.time.LocalDate

class ProjectionFormFake : IProjectionFormPort{
    override fun save(projection: ProjectionDTO): Boolean {
        TODO("Not yet implemented")
    }

    override fun update(projection: ProjectionDTO): Boolean {
        TODO("Not yet implemented")
    }

    override fun findById(id: Int): ProjectionDTO? {
        TODO("Not yet implemented")
    }

    override fun calculateQuote(
        period: KindPaymentsEnums,
        date: LocalDate,
        value: BigDecimal
    ): BigDecimal {
        TODO("Not yet implemented")
    }
}