package co.com.japl.module.paid.navigations

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import co.com.japl.module.paid.R

object Paids {

    fun navigate(codeAccount:Int,navController: NavController){
        val request = NavDeepLinkRequest.Builder.fromUri(navController.context.getString(R.string.navigate_paids,codeAccount).toUri()).build()
        navController.navigate(request)
    }

}