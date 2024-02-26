package co.com.japl.module.creditcard.navigations

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import co.com.japl.module.creditcard.R
import co.com.japl.ui.utils.DateUtils
import java.time.LocalDateTime

object ListBought {
    fun navigate(codeCreditCard:Int, cutOffDate:Short, cutOff: LocalDateTime, navigate:NavController){
        val request = NavDeepLinkRequest.Builder.fromUri(navigate.context.getString(R.string.navigate_list_bought,codeCreditCard,cutOffDate
            ,DateUtils.localDateToString(cutOff.toLocalDate())).toUri()).build()
        navigate.navigate(request)
    }
}