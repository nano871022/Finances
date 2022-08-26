package co.japl.android.myapplication.finanzas.putParams

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.pojo.QuoteCreditCard
import co.japl.android.myapplication.utils.CalcEnum
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
    }

    companion object{
        @JvmStatic
        fun newInstance(creditValue:BigDecimal,interest:Double,period:Long,quoteValue:BigDecimal,interestValue:BigDecimal,capitalValue:BigDecimal,navController:NavController){
            val parameters = bundleOf(
                Params.ARG_CREDIT_VALUE to creditValue.toString()
                , Params.ARG_TAX_VALUE to interest.toString()
                , Params.ARG_PERIOD_VALUE to period.toString()
                , Params.ARG_QUOTE_VALUE to quoteValue.toString()
                , Params.ARG_INTEREST_VALUE to interestValue.toString()
                , Params.ARG_CAPITAL_VALUE to capitalValue.toString())
            Log.d(this.javaClass.name,"$parameters")
            navController.navigate(R.id.action_save_quote_credit_variable,parameters)
        }

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
                quote.type = CalcEnum.VARIABLE
                return Optional.ofNullable(quote)
            }
            return Optional.empty()
        }
        fun toBack(navController:NavController){
            navController.navigate(R.id.action_quoteCreditSaveVariable_to_item_menu_side_quoteCreditVariable)
        }
    }
}