package co.japl.android.myapplication.putParams

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.putParams.TaxesParams.Params.ARG_PARAM1
import co.japl.android.myapplication.putParams.TaxesParams.Params.ARG_PARAM2

class TaxesParams() {
    object Params {
        val ARG_PARAM1 = "param1"
        val ARG_PARAM2 = "param2"
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
                return Pair(it.get(ARG_PARAM1).toString(),it.get(ARG_PARAM2).toString())
            }
        }
        fun toBack(navController: NavController){
            navController.popBackStack()
        }
    }
}