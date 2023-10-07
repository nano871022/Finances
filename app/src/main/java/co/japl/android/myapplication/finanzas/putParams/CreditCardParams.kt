package co.japl.android.myapplication.putParams

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.putParams.CreditCardParams.Params.ARG_PARAM_CODE
import java.util.*

class CreditCardParams(var parentFragmentManagers: FragmentManager) {
    object Params {
        val ARG_PARAM_CODE = "code_credit_card"
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String,navController:NavController) {
            val parameter = bundleOf(
                    ARG_PARAM_CODE to param1
            )
            navController.navigate(R.id.action_listCreditCard_to_createCreditCard,parameter)
        }

        fun newInstance(navController:NavController) {
            navController.navigate(R.id.action_listCreditCard_to_createCreditCard)
        }

        fun newInstanceFromQuote(navController:NavController) {
            navController.navigate(R.id.action_item_menu_side_boughtmade_to_createCreditCard)
        }

        @RequiresApi(Build.VERSION_CODES.N)
        fun download(argument:Bundle):Optional<String>{
            argument.let {
                return Optional.ofNullable(it.get(ARG_PARAM_CODE).toString())
            }
            return Optional.empty()
        }

        fun toBack(navController: NavController){
            navController.popBackStack()
        }
    }
}