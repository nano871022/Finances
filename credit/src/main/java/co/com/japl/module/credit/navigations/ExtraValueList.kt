package co.com.japl.module.credit.navigations

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import co.com.japl.module.credit.R

object ExtraValueList {

    fun list(creditCode:Int,nav: NavController){
        val uri = nav.context.getString(R.string.navigate_list_extra_amortization,creditCode).toUri()
        val deep = NavDeepLinkRequest.Builder.fromUri(uri).build()
        nav.navigate(uri)
    }
}