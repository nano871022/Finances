package co.com.japl.module.creditcard.params

import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import co.com.japl.module.creditcard.R
import java.util.*

class CreditCardParams(var parentFragmentManagers: FragmentManager) {
    object Params {
        val ARG_PARAM_CODE = "code_credit_card"
        val ARG_PARAM_CODE_CREDIT_CARD = "codeCreditCard"
        const val ARG_DEEPLINK = "android-support-nav:controller:deepLinkIntent"
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String,navController:NavController) {
            val parameter = bundleOf(
                    Params.ARG_PARAM_CODE to param1
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
            Log.d(javaClass.name,"=== download $argument ${argument.keySet().toSet()}")
            argument.let {
                if(it.get(Params.ARG_PARAM_CODE) != null) {
                    return Optional.ofNullable(it.get(Params.ARG_PARAM_CODE).toString())
                }else if(it[Params.ARG_DEEPLINK] != null){
                    return Optional.ofNullable(Uri.parse((it[Params.ARG_DEEPLINK] as Intent).dataString).getQueryParameter(Params.ARG_PARAM_CODE_CREDIT_CARD))

                }
            }
            return Optional.empty()
        }

        @RequiresApi(Build.VERSION_CODES.N)
        fun download(savedStateHandle: SavedStateHandle):Optional<String>{
            val code = savedStateHandle.get<String>(Params.ARG_PARAM_CODE)
            if(code != null){
                return Optional.ofNullable(code)
            }
            savedStateHandle.get<Intent>(Params.ARG_DEEPLINK)?.let { intent ->
                return Optional.ofNullable(intent.dataString?.toUri()?.getQueryParameter(Params.ARG_PARAM_CODE_CREDIT_CARD))
            }
            return Optional.empty()
        }

        fun toBack(navController: NavController){
            navController.popBackStack()
        }
    }
}