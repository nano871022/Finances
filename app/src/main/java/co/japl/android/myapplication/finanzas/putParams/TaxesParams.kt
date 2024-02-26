package co.japl.android.myapplication.putParams

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.putParams.TaxesParams.Params.ARG_CODE_CREDIT_CARD
import co.japl.android.myapplication.putParams.TaxesParams.Params.ARG_CODE_CREDIT_RATE
import co.japl.android.myapplication.putParams.TaxesParams.Params.ARG_PARAM1
import co.japl.android.myapplication.putParams.TaxesParams.Params.ARG_PARAM2

class TaxesParams() {
    object Params {
        val ARG_PARAM1 = "param1"
        val ARG_PARAM2 = "param2"
        val ARG_CODE_CREDIT_CARD = "codeCreditCard"
        val ARG_CODE_CREDIT_RATE = "codeCreditRate"
        const val ARG_DEEPLINK = "android-support-nav:controller:deepLinkIntent"
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String,navController:NavController) {
            val parameters = bundleOf(
                    ARG_PARAM1 to param1,
                    ARG_PARAM2 to param2
                )
            navController.navigate(R.id.action_item_menu_setting_taxes_to_taxes,parameters)
        }

        fun newInstanceFromQCC(navController:NavController) {
            navController.navigate(R.id.action_item_menu_side_boughtmade_to_taxes)
        }
        fun newInstance(navController:NavController) {
            navController.navigate(R.id.action_item_menu_setting_taxes_to_taxes)
        }
        fun download(argument: Bundle):Pair<String,String>{
            argument.let {
                var param1 = it.getString(ARG_PARAM1) ?: ""
                var param2 = it.getString(ARG_PARAM2) ?: ""

                if(it.containsKey(Params.ARG_DEEPLINK)){
                    val intent = it.get(Params.ARG_DEEPLINK) as Intent
                    if(Uri.parse(intent.dataString).getQueryParameter(Params.ARG_CODE_CREDIT_CARD) != null){
                        param1 = Uri.parse(intent.dataString).getQueryParameter(Params.ARG_CODE_CREDIT_CARD)!!
                    }
                    if(Uri.parse(intent.dataString).getQueryParameter(Params.ARG_CODE_CREDIT_RATE) != null){
                        param2 = Uri.parse(intent.dataString).getQueryParameter(Params.ARG_CODE_CREDIT_RATE)!!
                    }
                }

                return Pair(param1,param2)
            }
        }
        fun toBack(navController: NavController){
            navController.popBackStack()
        }
    }
}