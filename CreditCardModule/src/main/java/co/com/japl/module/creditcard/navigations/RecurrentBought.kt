package co.com.japl.module.creditcard.navigations

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import co.com.japl.module.creditcard.R

object RecurrentBought {
    fun navigate(codeCreditCard: Int, navigate: NavController) {
        val request = NavDeepLinkRequest.Builder.fromUri(navigate.context.getString(R.string.navigate_recurrent_list_bought, codeCreditCard).toUri()).build()
        navigate.navigate(request)
    }
}
