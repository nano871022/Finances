package co.com.japl.finances.iports.params

import android.content.Intent
import android.os.Bundle
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import co.com.japl.ui.utils.DateUtils
import co.com.japl.ui.R
import co.japl.android.finances.services.dto.CalcDTO

import co.com.japl.finances.iports.enums.AmortizationKindOfEnum
import co.com.japl.finances.iports.params.AdditionalCreditParams.Params
import com.google.gson.Gson
import java.time.LocalDate

class AmortizationTableParams {

    object params{
        val ARG_PARAM_CREDIT_VALUE = "credit_value"
        val ARG_PARAM_KIND_OF_AMORTIZATION = "kind_of_amortization"
    }

    companion object{
        fun newInstance(creditValue:CalcDTO,navController: NavController){
            val parameters = bundleOf(params.ARG_PARAM_CREDIT_VALUE to Gson().toJson(creditValue),params.ARG_PARAM_KIND_OF_AMORTIZATION to AmortizationKindOfEnum.EXTRA_VALUE_AMORTIZATION)
            navController.navigate(R.id.action_item_menu_side_listsave_to_amortizationTableFragment,parameters)
        }

        fun newInstanceQuotes(code:Long,navController:NavController){
            val parameters = bundleOf("CODE" to code)
            navController.navigate(R.id.action_list_bought_to_amortizationTableFragment,parameters)
        }

        fun download(argument: Bundle):Map<String,Any>{
            argument.let {
                if( it.containsKey(Params.PARAM_DEEPLINK) ){
                    val intent = (it.get(Params.PARAM_DEEPLINK) as Intent).dataString?.toUri()
                    val date = intent?.getQueryParameter("last_date")?.let{ DateUtils.toLocalDate(it) }
                    return@download mapOf<String,Any>(
                        "CREDIT_CODE" to (intent?.getQueryParameter("credit_code")?.toLong()?:0.toLong()),
                        "CODE" to (intent?.getQueryParameter("code")?.toLong()?:0.toLong()),
                        "LAST_DATE" to (date?: LocalDate.now())
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