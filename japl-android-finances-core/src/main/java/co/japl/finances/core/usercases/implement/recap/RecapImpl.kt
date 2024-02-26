package co.japl.finances.core.usercases.implement.recap

import co.com.japl.finances.iports.dtos.RecapDTO
import co.japl.finances.core.usercases.interfaces.common.ICreditCard
import co.japl.finances.core.usercases.interfaces.common.ICreditFix
import co.japl.finances.core.usercases.interfaces.common.IInput
import co.japl.finances.core.usercases.interfaces.common.IPaid
import co.japl.finances.core.usercases.interfaces.common.IProjections
import co.japl.finances.core.usercases.interfaces.common.IQuoteCreditCard
import co.japl.finances.core.usercases.interfaces.recap.IRecap
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

class RecapImpl @Inject constructor(
    private val projectionSvc: IProjections,
    private val creditSvc: ICreditFix,
    private val paidSvc: IPaid,
    private val quoteCreditCardSvc: IQuoteCreditCard,
    private val inputSvc: IInput,
    private val creditCardSvc: ICreditCard
): IRecap {
    override fun getTotalValues(): RecapDTO {
        val projection = projectionSvc.getTotalSavedAndQuote()
        val totalQuoteCredit = creditSvc.getTotalQuote(LocalDate.now())
        val totalPaid = paidSvc.getTotalPaid()
        val totalQuoteTC = quoteCreditCardSvc.getTotalQuote()
        val totalInputs = inputSvc.getTotalInputs() ?: BigDecimal.ZERO
        val warning = creditCardSvc.getAll().sumOf { it.warningValue }
        return RecapDTO(
            projectionSaved= projection.first.toDouble()
            , projectionNext = projection.second.toDouble()
            , totalQuoteCredit= totalQuoteCredit.toDouble()
            , totalPaid = totalPaid.toDouble()
            , totalInputs = totalInputs.toDouble()
            , totalQuoteCreditCard = totalQuoteTC.toDouble()
            , warningValueCreditCard = warning.toDouble())
    }

}