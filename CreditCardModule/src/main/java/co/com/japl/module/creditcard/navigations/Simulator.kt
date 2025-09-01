package co.com.japl.module.creditcard.navigations

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import co.com.japl.module.creditcard.R

object Simulator {
    fun navigate(code:Int, navController: NavController){
        val request = NavDeepLinkRequest.Builder
            .fromUri(navController.context
                .getString(R.string.navigate_form_simulator_amortization,code)
                .toUri())
            .build()
        navController.navigate(request)
    }
}