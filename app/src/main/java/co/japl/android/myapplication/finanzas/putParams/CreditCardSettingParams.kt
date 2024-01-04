package co.japl.android.myapplication.putParams

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.putParams.CreditCardSettingParams.Params.ARG_CODE_CREDIT_CARD
import co.japl.android.myapplication.putParams.CreditCardSettingParams.Params.ARG_ID

class CreditCardSettingParams() {
    object Params {
        const val ARG_CODE_CREDIT_CARD = "codeCreditCard"
        const val ARG_CODE_CREDIT_CARD_SETTING = "codeSetting"
        const val ARG_ID = "ID"
        const val ARG_DEEPLINK = "android-support-nav:controller:deepLinkIntent"

    }

    companion object {
        @JvmStatic
        fun newInstance(codeCreditCard: Int, navController:NavController) {
            Log.v(this.javaClass.name,"New Instance code credit card: $codeCreditCard")
        bundleOf(ARG_CODE_CREDIT_CARD to codeCreditCard).let {
            navController.navigate(
                R.id.action_listCreditCardSetting_to_creditCardSettingFragment,
                it
            )
        }
        }

        fun newInstance(codeCreditCard: Int, id:Int , navController:NavController) {
            Log.v(this.javaClass.name,"New Instance code credit card: $codeCreditCard Id: $id")
            val parameters = bundleOf(
                ARG_CODE_CREDIT_CARD to codeCreditCard,
                ARG_ID to id
            )
            navController.navigate(R.id.action_listCreditCardSetting_to_creditCardSettingFragment,parameters)
        }


        fun download(argument: Bundle?):Map<String,Int>{
            argument?.let {
                val map = HashMap<String,Int>()
                if(it.containsKey(ARG_CODE_CREDIT_CARD)){
                    map[ARG_CODE_CREDIT_CARD] = it.get(ARG_CODE_CREDIT_CARD).toString().toInt()
                }
                if(it.containsKey(ARG_ID)){
                    map[ARG_ID] = it.get(ARG_ID).toString().toInt()
                }
                if(it.containsKey(Params.ARG_DEEPLINK)){
                    val intent = it.get(Params.ARG_DEEPLINK) as Intent
                    if(Uri.parse(intent.dataString).getQueryParameter(Params.ARG_CODE_CREDIT_CARD) != null){
                        map[Params.ARG_CODE_CREDIT_CARD] = Uri.parse(intent.dataString).getQueryParameter(Params.ARG_CODE_CREDIT_CARD)!!.toInt()
                    }
                    if(Uri.parse(intent.dataString).getQueryParameter(Params.ARG_CODE_CREDIT_CARD_SETTING) != null){
                        map[Params.ARG_ID] = Uri.parse(intent.dataString).getQueryParameter(Params.ARG_CODE_CREDIT_CARD_SETTING)!!.toInt()
                    }
                }
                return map.also { Log.d(javaClass.name,"download $it") }
            }
            return HashMap<String,Int>()
        }

        fun toBack(codeCreditCard:Int,navController: NavController){
            navController.popBackStack()
        }
    }
}