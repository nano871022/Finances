package co.com.japl.module.creditcard.navigations

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import co.com.japl.module.creditcard.R
import co.com.japl.ui.utils.DateUtils
import java.time.LocalDateTime

object Bought {
    fun navigate(codeCreditCard:Int, cutOffDate:Short, cutOff: LocalDateTime, navigate:NavController){
        val request = NavDeepLinkRequest.Builder.fromUri(navigate.context.getString(R.string.navigate_list_bought,codeCreditCard,cutOffDate
            ,DateUtils.localDateToString(cutOff.toLocalDate())).toUri()).build()
        navigate.navigate(request)
    }

    fun navigatePeriodList(codeCreditCard:Short,navigate: NavController){
        val request = NavDeepLinkRequest.Builder.fromUri(navigate.context.getString(R.string.navigate_list_bought_period,codeCreditCard).toUri()).build()
        navigate.navigate(request)
    }

    fun navigateAddBought(codeCreditCard:Short,navigate: NavController){
        val request = NavDeepLinkRequest.Builder.fromUri(navigate.context.getString(R.string.navigate_bought,codeCreditCard).toUri()).build()
        navigate.navigate(request)
    }

    fun navigateAddBoughtWallet(codeCreditCard:Short,navigate: NavController){
        val request = NavDeepLinkRequest.Builder.fromUri(navigate.context.getString(R.string.navigate_bought_wallet,codeCreditCard).toUri()).build()
        navigate.navigate(request)
    }

    fun navigateAddAdvance(codeCreditCard:Short,navigate: NavController){
        val request = NavDeepLinkRequest.Builder.fromUri(navigate.context.getString(R.string.navigate_bought_cash_advance,codeCreditCard).toUri()).build()
        navigate.navigate(request)
    }

}