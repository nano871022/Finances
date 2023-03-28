package co.japl.android.myapplication.finanzas.bussiness.interfaces

import java.math.BigDecimal
import java.time.LocalDate

interface ICreditFix {

    fun getInterestAll(date:LocalDate):BigDecimal
    fun getCapitalAll(date:LocalDate):BigDecimal
    fun getQuoteAll():BigDecimal
    fun getPendingToPayAll(date:LocalDate):BigDecimal
    fun getAdditionalAll():BigDecimal

}