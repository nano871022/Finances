package co.com.japl.module.paid.navigations

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import co.com.japl.module.paid.R

object EmailPaid {
    fun navigate(codeEmailPaid: Int, navigate: NavController) {
        val request = NavDeepLinkRequest.Builder.fromUri(navigate.context.getString(R.string.navigate_email_paid_form_code, codeEmailPaid).toUri()).build()
        navigate.navigate(request)
    }

    fun navigate(navigate: NavController) {
        val request = NavDeepLinkRequest.Builder.fromUri(navigate.context.getString(R.string.navigate_email_paid_form).toUri()).build()
        navigate.navigate(request)
    }
}
