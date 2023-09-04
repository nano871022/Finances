package co.japl.android.myapplication.finanzas.putParams

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CalcDTO
import co.japl.android.myapplication.bussiness.interfaces.Calc
import co.japl.android.myapplication.finanzas.enums.AmortizationKindOfEnum
import com.google.gson.Gson

class AmortizationTableParams {

    object params{
        val ARG_PARAM_CREDIT_VALUE = "credit_value"
        val ARG_PARAM_QUOTE_PAID = "quote_paid"
        val ARG_PARAM_QUOTE1_NOT_PAID = "quote_1_not_paid"
        val ARG_PARAM_HAS_DIFFER_INSTALLMENT = "has_differ_installment"
        val ARG_PARAM_BOUGHT_ID = "bought_id"
        val ARG_PARAM_MONTHS_CALC = "months_calc"
        val ARG_PARAM_KIND_OF_AMORTIZATION = "kind_of_amortization"
    }

    companion object{
        fun newInstance(creditValue:CalcDTO,navController: NavController){
            val parameters = bundleOf(params.ARG_PARAM_CREDIT_VALUE to Gson().toJson(creditValue),params.ARG_PARAM_KIND_OF_AMORTIZATION to AmortizationKindOfEnum.EXTRA_VALUE_AMORTIZATION)
            navController.navigate(R.id.action_item_menu_side_listsave_to_amortizationTableFragment,parameters)
        }

        fun newInstanceFix(creditValue:CalcDTO,navController: NavController){
            val parameters = bundleOf(params.ARG_PARAM_CREDIT_VALUE to Gson().toJson(creditValue),params.ARG_PARAM_KIND_OF_AMORTIZATION to AmortizationKindOfEnum.EXTRA_VALUE_AMORTIZATION)
            navController.navigate(R.id.action_item_menu_side_quoteCredit_to_amortizationTableFragment,parameters)
        }

        fun newInstanceVariable(creditValue:CalcDTO,navController: NavController){
            val parameters = bundleOf(params.ARG_PARAM_CREDIT_VALUE to Gson().toJson(creditValue),params.ARG_PARAM_KIND_OF_AMORTIZATION to AmortizationKindOfEnum.EXTRA_VALUE_AMORTIZATION)
            navController.navigate(R.id.action_item_menu_side_quoteCreditVariable_to_amortizationTableFragment,parameters)
        }

        fun newInstanceQuotes(creditValue:CalcDTO,quotesPaid:Long,quote1NotPaid:Boolean,navController: NavController){
            val parameters = bundleOf(params.ARG_PARAM_CREDIT_VALUE to Gson().toJson(creditValue),params.ARG_PARAM_QUOTE_PAID to quotesPaid,params.ARG_PARAM_QUOTE1_NOT_PAID to quote1NotPaid,params.ARG_PARAM_KIND_OF_AMORTIZATION to AmortizationKindOfEnum.EXTRA_VALUE_AMORTIZATION)
            navController.navigate(R.id.action_list_bought_to_amortizationTableFragment,parameters)
        }

        fun newInstanceQuotes(creditValue:CalcDTO,id:Long,quotesPaid:Long,quote1NotPaid:Boolean,hasDifferInstallment:Boolean,monthsCalc:Long,navController: NavController){
            val parameters = bundleOf(params.ARG_PARAM_CREDIT_VALUE to Gson().toJson(creditValue)
                ,params.ARG_PARAM_QUOTE_PAID to quotesPaid
                ,params.ARG_PARAM_QUOTE1_NOT_PAID to quote1NotPaid
                ,params.ARG_PARAM_HAS_DIFFER_INSTALLMENT to hasDifferInstallment
                ,params.ARG_PARAM_BOUGHT_ID to id
                ,params.ARG_PARAM_MONTHS_CALC to monthsCalc
                ,params.ARG_PARAM_KIND_OF_AMORTIZATION to AmortizationKindOfEnum.EXTRA_VALUE_AMORTIZATION_QUOTE_CREDIT_CARD)
            navController.navigate(R.id.action_list_bought_to_amortizationTableFragment,parameters)
        }

        fun download(argument: Bundle):Map<String,Any>{
            argument.let {
                return mapOf(params.ARG_PARAM_CREDIT_VALUE to Gson().fromJson(it.get(params.ARG_PARAM_CREDIT_VALUE) as String,CalcDTO::class.java)
                ,params.ARG_PARAM_QUOTE_PAID to it.getLong(params.ARG_PARAM_QUOTE_PAID)
                ,params.ARG_PARAM_QUOTE1_NOT_PAID to (it.getBoolean(params.ARG_PARAM_QUOTE1_NOT_PAID)?:false)
                ,params.ARG_PARAM_HAS_DIFFER_INSTALLMENT to (it.getBoolean(params.ARG_PARAM_HAS_DIFFER_INSTALLMENT)?:false)
                ,params.ARG_PARAM_BOUGHT_ID to it.getLong(params.ARG_PARAM_BOUGHT_ID)
                ,params.ARG_PARAM_MONTHS_CALC to it.getLong(params.ARG_PARAM_MONTHS_CALC)
                ,params.ARG_PARAM_KIND_OF_AMORTIZATION to it.get(params.ARG_PARAM_KIND_OF_AMORTIZATION) as AmortizationKindOfEnum)
            }
        }
        fun toBack(navController: NavController){
            navController.popBackStack()
        }
    }

}