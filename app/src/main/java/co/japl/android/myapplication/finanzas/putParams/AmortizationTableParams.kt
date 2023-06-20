package co.japl.android.myapplication.finanzas.putParams

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CalcDTO
import co.japl.android.myapplication.bussiness.interfaces.Calc
import com.google.gson.Gson

class AmortizationTableParams {

    object params{
        val ARG_PARAM_CREDIT_VALUE = "credit_value"
        val ARG_PARAM_QUOTE_PAID = "quote_paid"
        val ARG_PARAM_QUOTE1_NOT_PAID = "quote_1_not_paid"
    }

    companion object{
        fun newInstance(creditValue:CalcDTO,navController: NavController){
            val parameters = bundleOf(params.ARG_PARAM_CREDIT_VALUE to Gson().toJson(creditValue))
            navController.navigate(R.id.action_item_menu_side_listsave_to_amortizationTableFragment,parameters)
        }

        fun newInstanceFix(creditValue:CalcDTO,navController: NavController){
            val parameters = bundleOf(params.ARG_PARAM_CREDIT_VALUE to Gson().toJson(creditValue))
            navController.navigate(R.id.action_item_menu_side_quoteCredit_to_amortizationTableFragment,parameters)
        }

        fun newInstanceVariable(creditValue:CalcDTO,navController: NavController){
            val parameters = bundleOf(params.ARG_PARAM_CREDIT_VALUE to Gson().toJson(creditValue))
            navController.navigate(R.id.action_item_menu_side_quoteCreditVariable_to_amortizationTableFragment,parameters)
        }

        fun newInstanceQuotes(creditValue:CalcDTO,quotesPaid:Long,quote1NotPaid:Boolean,navController: NavController){
            val parameters = bundleOf(params.ARG_PARAM_CREDIT_VALUE to Gson().toJson(creditValue),params.ARG_PARAM_QUOTE_PAID to quotesPaid,params.ARG_PARAM_QUOTE1_NOT_PAID to quote1NotPaid)
            navController.navigate(R.id.action_list_bought_to_amortizationTableFragment,parameters)
        }

        fun download(argument: Bundle):Triple<CalcDTO,Long,Boolean>{
            argument.let {
                return Triple(Gson().fromJson(it.get(params.ARG_PARAM_CREDIT_VALUE) as String,CalcDTO::class.java), it.getLong(params.ARG_PARAM_QUOTE_PAID),it.getBoolean(params.ARG_PARAM_QUOTE1_NOT_PAID)?:false)
            }
        }
        fun toBack(navController: NavController){
            navController.popBackStack()
        }
    }

}