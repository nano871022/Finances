package co.japl.android.myapplication.finanzas.putParams

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import co.japl.android.myapplication.R

class AccountParams {
    object PARAMS{
        const val PARAM_ID_ACCOUNT = "id_account"
        const val ARG_DEEPLINK = "android-support-nav:controller:deepLinkIntent"
    }
    companion object {

        fun newInstance(id:Int,navController: NavController) {
            Log.d(javaClass.name,"Account ID: $id")
            val parameters = bundleOf( PARAMS.PARAM_ID_ACCOUNT to id)
            navController.navigate(R.id.action_accountFragment_to_inputListFragment,parameters)
        }

        fun newInstance(navController: NavController){
            navController.navigate(R.id.action_menu_item_paids_to_accountFragment)
        }

        fun newInstanceDeep(codeAccount:Int,navController: NavController){
            val request = NavDeepLinkRequest.Builder.fromUri("android-app://co.com.japl.finanzas.module.app/accounts/inputs?id_account=$codeAccount".toUri()).build()
            navController.navigate(request)
        }

        fun newInstanceDeep(navController: NavController){
            val request = NavDeepLinkRequest.Builder.fromUri("android-app://co.com.japl.finanzas.module.app/accounts/inputs".toUri()).build()
            navController.navigate(request)
        }

        fun download(parameters:Bundle):Int{
            parameters.let{
                if(it.containsKey(InputListParams.PARAMS.ARG_DEEPLINK)) {
                    val intent = it.get(InputListParams.PARAMS.ARG_DEEPLINK) as Intent
                    if(Uri.parse(intent.dataString).getQueryParameter(PARAMS.PARAM_ID_ACCOUNT) != null){
                        return Uri.parse(intent.dataString).getQueryParameter(PARAMS.PARAM_ID_ACCOUNT)!!.toInt()
                    }
                }
                return parameters.getInt(PARAMS.PARAM_ID_ACCOUNT)
            }
        }

        fun toBack(navController: NavController)= navController.popBackStack()
    }
}