package co.com.japl.module.creditcard.navigations

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import co.com.japl.module.creditcard.R

object SMSCreditCard {

    fun navigate(code:Int,navigate: NavController){
        val request = NavDeepLinkRequest.Builder.fromUri(navigate.context.getString(R.string.navigate_form_sms_credit_rate_edit,code).toUri()).build()
        navigate.navigate(request)
    }

    fun navigate(navigate: NavController){
        val request = NavDeepLinkRequest.Builder.fromUri(navigate.context.getString(R.string.navigate_form_sms_credit_rate).toUri()).build()
        navigate.navigate(request)
    }
}