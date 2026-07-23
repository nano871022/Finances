package co.com.japl.module.creditcard.navigations

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import co.com.japl.module.creditcard.R

object LoginNavigation {
    fun navigateToLogin(navController: NavController) {
        val request = NavDeepLinkRequest.Builder
            .fromUri(navController.context.getString(R.string.navigate_to_login).toUri())
            .build()
        navController.navigate(request)
    }
}
