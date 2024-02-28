package co.japl.android.myapplication.finanzas.putParams

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import co.japl.android.myapplication.R
import java.util.*

class CashAdvanceParams {

    object params{
        val ARG_PARAM_CODE_CREDIT_CARD = "cod_credit_card"
        val PARAM_DEEPLINK = "android-support-nav:controller:deepLinkIntent"
    }

    companion object{
        fun newInstance(code:Int,navController: NavController){
            val parameters = bundleOf(params.ARG_PARAM_CODE_CREDIT_CARD to code.toString())
            navController.navigate(R.id.action_item_menu_side_boughtmade_to_cash_advance_fragment,parameters)
        }

        fun newInstanceFloat(code:Int,navController: NavController){
            val parameters = bundleOf(params.ARG_PARAM_CODE_CREDIT_CARD to code.toString())
            navController.navigate(R.id.action_list_bought_to_cash_advance_fragment,parameters)
        }
        fun download(argument:Bundle):String{
            argument.let {
                if(it.containsKey(params.PARAM_DEEPLINK)) {
                    val intent = it.get(params.PARAM_DEEPLINK) as Intent
                    Uri.parse(intent.dataString).getQueryParameter(params.ARG_PARAM_CODE_CREDIT_CARD)?.let {
                        return it
                    }
                }
                return it.get(params.ARG_PARAM_CODE_CREDIT_CARD).toString()
            }
        }
        fun toBack(navController: NavController){
            navController.popBackStack()
        }
    }
}