package co.com.japl.module.creditcard.navigations

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import co.com.japl.module.creditcard.R

object ListCreditCardSettings {

    fun navigate(codecreditCard:Int,navigate:NavController){
        val request = NavDeepLinkRequest.Builder.fromUri(navigate.context.getString(R.string.navigate_list_credit_card_setting,codecreditCard).toUri()).build()
        navigate.navigate(request)
    }
}