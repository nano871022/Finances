package co.com.japl.module.credit.controllers.forms

import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.enums.KindPaymentsEnums
import co.japl.android.myapplication.utils.NumbersUtil
import java.math.BigDecimal
import java.time.LocalDate

data class CreditFormState (
    val kindPayment: Triple<Int,String,KindPaymentsEnums?> = Triple(0,"",null),
    val kindPaymentError: String? = null,
    val creditDate: LocalDate = LocalDate.now(),
    val creditDateError: String? = null,
    val name: String = "",
    val nameError: String? = null,
    val value: String = "",
    val valueAmt: BigDecimal = NumbersUtil.toBigDecimal(value),
    val valueError: String? = null,
    val rate: String = "",
    val rateAmt: Double = 0.0,
    val rateError: String? = null,
    val kindRate: Triple<Int,String, KindOfTaxEnum?> = Triple(0,"",null),
    val kindRateError: String? = null,
    val month: Int = 0,
    val monthError: String? = null,
    val quoteCredit: BigDecimal = BigDecimal.ZERO,
    val isSubmitting:Boolean=false,
    val isFormValid:Boolean = kindPaymentError == null && creditDateError == null && nameError == null && valueError == null && rateError == null && kindRateError == null && monthError == null
)