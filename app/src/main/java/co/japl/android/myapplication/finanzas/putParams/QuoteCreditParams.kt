package co.japl.android.myapplication.finanzas.putParams

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.pojo.QuoteCreditCard
import java.math.BigDecimal
import java.util.*

class QuoteCreditParams {
    object Params{
        val ARG_CREDIT_VALUE = "creditValue"
        val ARG_INTEREST_VALUE = "interest"
        val ARG_PERIOD_VALUE = "period"
        val ARG_QUOTE_VALUE = "quoteValue"
    }

    companion object{
        @JvmStatic
        fun newInstance(creditValue:BigDecimal,interest:Double,period:Long,quoteValue:BigDecimal,navController:NavController){
            val parameters = bundleOf("creditValue" to creditValue.toString()
                ,"interest" to interest.toString()
                ,"period" to period.toString()
                ,"quoteValue" to quoteValue.toString())
            navController.navigate(R.id.action_save_credit_card,parameters)
        }

        @RequiresApi(Build.VERSION_CODES.N)
        fun download(arguments:Bundle):Optional<QuoteCreditCard>{
            arguments?.let {
                val quote = QuoteCreditCard()
                quote.value = Optional.ofNullable(it.get("creditValue").toString().toBigDecimal())
                quote.tax = Optional.ofNullable(it.get("interest").toString().toDouble())
                quote.period = Optional.ofNullable(it.get("period").toString().toLong())
                quote.response = Optional.ofNullable( it.get("quoteValue").toString().toBigDecimal())
                return Optional.ofNullable(quote)
            }
            return Optional.empty()
        }
        fun toBack(navController:NavController){
            navController.navigate(R.id.action_quote_credit)
        }
    }
}