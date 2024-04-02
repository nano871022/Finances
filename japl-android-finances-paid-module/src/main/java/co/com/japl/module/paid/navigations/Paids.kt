package co.com.japl.module.paid.navigations

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import co.com.japl.module.paid.R
import java.time.YearMonth
import java.time.format.DateTimeFormatter

object Paids {

    fun navigate(codeAccount:Int, period: YearMonth, navController: NavController){
        val period = period.format(DateTimeFormatter.ofPattern("yyyy-MM"))
        val request = NavDeepLinkRequest.Builder.fromUri(navController.context.getString(R.string.navigate_paids,codeAccount,period).toUri()).build()
        navController.navigate(request)
    }

}