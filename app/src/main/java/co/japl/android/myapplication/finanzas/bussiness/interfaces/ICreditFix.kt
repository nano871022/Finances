package co.japl.android.myapplication.finanzas.bussiness.interfaces

import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.CreditDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.PeriodCreditDTO
import java.math.BigDecimal
import java.time.LocalDate

interface ICreditFix: SaveSvc<CreditDTO>, ISaveSvc<CreditDTO> {

    fun getInterestAll(date:LocalDate):BigDecimal
    fun getCapitalAll(date:LocalDate):BigDecimal
    fun getQuoteAll(date: LocalDate):BigDecimal
    fun getPendingToPayAll(date:LocalDate):BigDecimal
    fun getAdditionalAll(date:LocalDate):BigDecimal
    fun getPeriods():List<PeriodCreditDTO>
    fun getTotalQuote(date: LocalDate):BigDecimal
    fun getCurrentBoughtCredits(date:LocalDate):List<CreditDTO>
}