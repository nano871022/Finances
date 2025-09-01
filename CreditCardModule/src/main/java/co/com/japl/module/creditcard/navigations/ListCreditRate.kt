package co.com.japl.module.creditcard.navigations

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import co.com.japl.module.creditcard.R

object ListCreditRate {
    fun navigate(navigate:NavController){
        val request = NavDeepLinkRequest.Builder.fromUri(navigate.context.getString(R.string.navigate_list_credit_rate).toUri()).build()
        navigate.navigate(request)
    }

    fun navigate_list(navigate:NavController){
        val request = NavDeepLinkRequest.Builder.fromUri(navigate.context.getString(R.string.navigate_list_credit_rates).toUri()).build()
        navigate.navigate(request)
    }

    fun navigate(codeCreditCard:Int,navigate:NavController){
        val request = NavDeepLinkRequest.Builder.fromUri(navigate.context.getString(R.string.navigate_list_credit_rate_with_code_credit_card,codeCreditCard).toUri()).build()
        navigate.navigate(request)
    }

    fun navigate(codeCreditCard:Int,codeCreditRate:Int,navigate:NavController){
        val request = NavDeepLinkRequest.Builder.fromUri(navigate.context.getString(R.string.navigate_list_credit_rate_with_code_credit_card_and_cod_credit_rate,codeCreditCard,codeCreditRate).toUri()).build()
        navigate.navigate(request)
    }
}