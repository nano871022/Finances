package co.com.japl.module.paid.params

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import co.com.japl.module.paid.R

class SmsPaidParams(var parentFragmentManagers: FragmentManager) {
    object Params {
        val ARG_PARAM_CODE = "code_sms_paid"
        const val ARG_DEEPLINK = "android-support-nav:controller:deepLinkIntent"
    }
    companion object {
        @JvmStatic
        fun newInstance(code: Int,navController:NavController) {
            val parameter = bundleOf(Params.ARG_PARAM_CODE to code)
            navController.navigate(R.id.action_sms_list_credit_card_to_smsFragment,parameter)
        }

        fun newInstance(navController:NavController) {
            navController.navigate(R.id.action_sms_list_credit_card_to_smsFragment)
        }


        fun download(argument:Bundle):Int?{
            Log.d(javaClass.name,"=== download $argument ${argument.keySet().toSet()}")
            argument.let {
                if(it.containsKey(Params.ARG_PARAM_CODE)) {
                    return it.getInt(Params.ARG_PARAM_CODE)
                }else if(it.containsKey(Params.ARG_DEEPLINK)){
                    return Uri.parse((it.get(Params.ARG_DEEPLINK) as Intent).dataString).getQueryParameter(Params.ARG_PARAM_CODE)?.let{
                        it.toInt()
                    }
                }
            }
            return null
        }
    }
}