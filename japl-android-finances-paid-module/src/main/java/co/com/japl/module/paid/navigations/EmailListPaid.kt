package co.com.japl.module.paid.navigations

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import co.com.japl.module.paid.R

object EmailListPaid {
    fun navigate(navigate: NavController) {
        val request = NavDeepLinkRequest.Builder.fromUri(navigate.context.getString(R.string.navigate_email_paid_list).toUri()).build()
        navigate.navigate(request)
    }
}
