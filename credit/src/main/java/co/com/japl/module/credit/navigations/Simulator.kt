package co.com.japl.module.credit.navigations

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import co.com.japl.module.credit.R

object Simulator {
    fun navigate(code:Int, navController: NavController){
        val request = NavDeepLinkRequest.Builder
            .fromUri(navController.context
                .getString(R.string.navigate_form_simulator_amortization_fix,code)
                .toUri())
            .build()
        navController.navigate(request)
    }
}