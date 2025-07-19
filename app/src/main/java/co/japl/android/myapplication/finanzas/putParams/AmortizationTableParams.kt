package co.japl.android.myapplication.finanzas.putParams

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CalcDTO
import co.japl.android.myapplication.bussiness.interfaces.Calc
import co.japl.android.myapplication.finanzas.enums.AmortizationKindOfEnum
import co.japl.android.myapplication.finanzas.putParams.AdditionalCreditParams.Params
import com.google.gson.Gson
import java.net.URLDecoder
import java.nio.charset.Charset

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
            navController.navigate(R.id.action_item_menu_side_simulatorCredit_to_amortizationTableFixFragment,parameters)
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

        fun newInstanceQuotes(code:Long,navController:NavController){
            val parameters = bundleOf("CODE" to code)
            navController.navigate(R.id.action_list_bought_to_amortizationTableFragment,parameters)
        }

        fun download(argument: Bundle):Map<String,Any>{
            argument.let {
                if( it.containsKey(Params.PARAM_DEEPLINK) ){
                    val intent = (it.get(Params.PARAM_DEEPLINK) as Intent).dataString?.toUri()

                    return@download mapOf<String,Any>(
                        "CODE" to (intent?.getQueryParameter("code")?.toLong()?:0.toLong())
                    )
                }else{
                    val code = it.getLong("CODE")
                    return@download mapOf<String,Any>(
                        "CODE" to code
                    )
                }
            }
            return mapOf()
        }

        fun toBack(navController: NavController){
            navController.popBackStack()
        }
    }

}