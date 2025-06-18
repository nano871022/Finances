package co.com.japl.finances.iports.inbounds.paid

import co.com.japl.finances.iports.dtos.ProjectionDTO
import co.com.japl.finances.iports.enums.KindPaymentsEnums
import java.math.BigDecimal
import java.time.LocalDate

interface IProjectionFormPort {

    fun save(projection: ProjectionDTO): Boolean

    fun update(projection: ProjectionDTO): Boolean

    fun findById(id: Int): ProjectionDTO?

    fun calculateQuote(period: KindPaymentsEnums, date: LocalDate, value: BigDecimal): BigDecimal

}