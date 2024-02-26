package co.japl.android.myapplication.finanzas.putParams

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import co.japl.android.myapplication.R

class InputListParams {
    object PARAMS {
        const val PARAM_ACCOUNT_CODE = "account_code"
        const val ARG_DEEPLINK = "android-support-nav:controller:deepLinkIntent"
    }
        companion object {

            fun newInstance(accountCode:Int,navController: NavController) {
                val parameters = bundleOf(PARAMS.PARAM_ACCOUNT_CODE to accountCode)
                navController.navigate(R.id.action_inputListFragment_to_inputFragment,parameters)
            }

            fun newInstanceDeeplink(accountCode: Int,navController: NavController){
                val request = NavDeepLinkRequest.Builder.fromUri("android-app://co.com.japl.finanzas.module.app/accounts/input?${PARAMS.PARAM_ACCOUNT_CODE}=$accountCode".toUri()).build()
                navController.navigate(request)
            }

            fun download(parameters: Bundle):Int {
                parameters.let{
                    if(it.containsKey(PARAMS.ARG_DEEPLINK)) {
                        val intent = it.get(PARAMS.ARG_DEEPLINK) as Intent
                        if(Uri.parse(intent.dataString).getQueryParameter(PARAMS.PARAM_ACCOUNT_CODE) != null){
                            return Uri.parse(intent.dataString).getQueryParameter(PARAMS.PARAM_ACCOUNT_CODE)!!.toInt()
                        }
                    }
                    return parameters.getInt(PARAMS.PARAM_ACCOUNT_CODE)
                }
            }

            fun toBack(navController: NavController)=  navController.popBackStack()
        }
}