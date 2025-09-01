package co.japl.android.myapplication.finanzas.putParams

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.pojo.QuoteCreditCard
import co.japl.android.myapplication.finanzas.enums.CalcEnum
import java.math.BigDecimal
import java.util.*

class QuoteCreditVariablesParams {
    object Params{
        val ARG_CREDIT_VALUE = "creditValue"
        val ARG_TAX_VALUE = "interest"
        val ARG_PERIOD_VALUE = "period"
        val ARG_QUOTE_VALUE = "quoteValue"
        val ARG_INTEREST_VALUE = "interestValue"
        val ARG_CAPITAL_VALUE = "capitalValue"
        val ARG_KIND_OF_TAX = "kind_of_tax"
    }

    companion object{

        @RequiresApi(Build.VERSION_CODES.N)
        fun download(arguments:Bundle):Optional<QuoteCreditCard>{
            arguments?.let {
                val quote = QuoteCreditCard()
                quote.value = Optional.ofNullable(it.get(Params.ARG_CREDIT_VALUE).toString().toBigDecimal())
                quote.tax = Optional.ofNullable(it.get(Params.ARG_TAX_VALUE).toString().toDouble())
                quote.period = Optional.ofNullable(it.get(Params.ARG_PERIOD_VALUE).toString().toLong())
                quote.response = Optional.ofNullable( it.get(Params.ARG_QUOTE_VALUE).toString().toBigDecimal())
                quote.interestValue = Optional.ofNullable( it.get(Params.ARG_INTEREST_VALUE).toString().toBigDecimal())
                quote.capitalValue = Optional.ofNullable( it.get(Params.ARG_CAPITAL_VALUE).toString().toBigDecimal())
                quote.kindOfTax = Optional.of(it.get(Params.ARG_KIND_OF_TAX).toString())
                quote.type = CalcEnum.VARIABLE
                return Optional.ofNullable(quote)
            }
            return Optional.empty()
        }
        fun toBack(navController:NavController){
            navController.popBackStack()
        }
    }
}