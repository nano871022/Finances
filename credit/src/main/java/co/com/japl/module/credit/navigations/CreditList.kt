package co.com.japl.module.credit.navigations

import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import co.com.japl.finances.iports.dtos.CreditDTO
import co.com.japl.finances.iports.utils.BigDecimalSerializer
import co.com.japl.module.credit.R
import co.com.japl.ui.utils.DateUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.math.BigDecimal
import java.time.LocalDate

object CreditList {
    fun amortization(credit:CreditDTO,date:LocalDate,navController: NavController){
        val dateLast = DateUtils.localDateToString(date)
        val uri = navController.context.getString(R.string.navigate_amortization,credit.id,dateLast).toUri()
        val request = NavDeepLinkRequest.Builder.fromUri(uri).build()
        navController.navigate(request)
    }

    fun additional(id:Int,navController: NavController){
        val request = NavDeepLinkRequest.Builder.fromUri(navController.context.getString(R.string.navigate_additional,id).toUri()).build()
        navController.navigate(request)
    }

    fun addCredit(navController: NavController){
        val request = NavDeepLinkRequest.Builder.fromUri(navController.context.getString(R.string.navigate_add_credit).toUri()).build()
        navController.navigate(request)
    }

    fun detailCredits(navController: NavController){
        val request = NavDeepLinkRequest.Builder.fromUri(navController.context.getString(R.string.navigate_detail_credits).toUri()).build()
        navController.navigate(request)
    }

    fun periodCredits(navController: NavController){
        val request = NavDeepLinkRequest.Builder.fromUri(navController.context.getString(R.string.navigate_period_credits).toUri()).build()
        navController.navigate(request)

    }
}