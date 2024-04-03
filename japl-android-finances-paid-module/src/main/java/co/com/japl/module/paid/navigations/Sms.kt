package co.com.japl.module.paid.navigations

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import co.com.japl.module.paid.R

object Sms {

    fun navigate(code:Int,navController: NavController){
        val request = NavDeepLinkRequest.Builder.fromUri(navController.context.getString(R.string.navigate_sms_form_code,code).toUri()).build()
        navController.navigate(request)
    }

    fun navigate(navController: NavController){
        val request = NavDeepLinkRequest.Builder.fromUri(navController.context.getString(R.string.navigate_sms_form).toUri()).build()
        navController.navigate(request)
    }
}