package co.japl.android.myapplication.putParams

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.controller.Taxes
import co.japl.android.myapplication.putParams.CreditCardSettingParams.Params.ARG_CODE_CREDIT_CARD
import co.japl.android.myapplication.putParams.CreditCardSettingParams.Params.ARG_ID

class ListCreditCardSettingParams() {
    object Params {
        val ARG_CODE_CREDIT_CARD = "codeCreditCard"
    }
    companion object {
        @JvmStatic
        fun newInstance(codeCreditCard: Int, navController:NavController) {
        val parameters = bundleOf(
                ARG_CODE_CREDIT_CARD to codeCreditCard,
                )
            navController.navigate(R.id.action_createCreditCard_to_listCreditCardSetting,parameters)
        }

        fun download(argument: Bundle?):Map<String,Int>{
            argument?.let {
                val map = HashMap<String,Int>()
                if(it.get(ARG_CODE_CREDIT_CARD) != null){
                    map[ARG_CODE_CREDIT_CARD.toString()] = it.get(ARG_CODE_CREDIT_CARD).toString().toInt()
                }
                map
            }
            return HashMap<String,Int>()
        }

        fun toBack(navController: NavController){
            navController.navigate(R.id.action_listCreditCardSetting_to_createCreditCard)
        }
    }
}