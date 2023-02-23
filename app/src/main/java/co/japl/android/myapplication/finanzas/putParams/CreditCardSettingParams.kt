package co.japl.android.myapplication.putParams

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.controller.Taxes
import co.japl.android.myapplication.putParams.CreditCardSettingParams.Params.ARG_CODE_CREDIT_CARD
import co.japl.android.myapplication.putParams.CreditCardSettingParams.Params.ARG_ID

class CreditCardSettingParams() {
    object Params {
        val ARG_CODE_CREDIT_CARD = "codeCreditCard"
        val ARG_ID = "ID"
    }
    companion object {
        @JvmStatic
        fun newInstance(codeCreditCard: Int, navController:NavController) {
        val parameters = bundleOf(
                ARG_CODE_CREDIT_CARD to codeCreditCard,
                )
            navController.navigate(R.id.action_listCreditCardSetting_to_creditCardSettingFragment,parameters)
        }

        fun newInstance(codeCreditCard: Int, id:Int , navController:NavController) {
            val parameters = bundleOf(
                ARG_CODE_CREDIT_CARD to codeCreditCard,
                ARG_ID to id
            )
            navController.navigate(R.id.action_listCreditCardSetting_to_creditCardSettingFragment,parameters)
        }


        fun download(argument: Bundle?):Map<String,Int>{
            argument?.let {
                val map = HashMap<String,Int>()
                if(it.get(ARG_CODE_CREDIT_CARD) != null){
                    map[ARG_CODE_CREDIT_CARD.toString()] = it.get(ARG_CODE_CREDIT_CARD).toString().toInt()
                }
                if(it.get(ARG_ID) != null){
                    map[ARG_ID.toString()] = it.get(ARG_ID).toString().toInt()
                }
                map
            }
            return HashMap<String,Int>()
        }

        fun toBack(navController: NavController){
            navController.navigate(R.id.action_creditCardSettingFragment_to_listCreditCardSetting)
        }
    }
}