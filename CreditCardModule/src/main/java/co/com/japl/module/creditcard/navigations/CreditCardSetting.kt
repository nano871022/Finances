package co.com.japl.module.creditcard.navigations

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import co.com.japl.module.creditcard.R

object CreditCardSetting {

    fun navigate(codeCreditCard:Int,navController: NavController){
        val request = NavDeepLinkRequest.Builder.fromUri(navController.context.getString(R.string.navigate_credit_card_setting_create,codeCreditCard).toUri()).build()
        navController.navigate(request)
    }

    fun navigate(codeCreditCard:Int, codeSetting:Int,navController: NavController){
        val request = NavDeepLinkRequest.Builder.fromUri(navController.context.getString(R.string.navigate_credit_card_setting,codeSetting,codeCreditCard).toUri()).build()
        navController.navigate(request)
    }
}