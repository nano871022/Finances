package co.com.japl.module.paid.navigations

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import co.com.japl.module.paid.R

object Account {

    fun navigate(codeAccount:Int, navController: NavController){
        val request = NavDeepLinkRequest.Builder.fromUri(
            navController.context.getString(R.string.navigate_account_code,codeAccount).toUri())
            .build()
        navController.navigate(request)
    }

    fun navigate( navController: NavController){
        val request = NavDeepLinkRequest.Builder.fromUri(
            navController.context.getString(R.string.navigate_account).toUri())
            .build()
        navController.navigate(request)
    }
}