package co.com.japl.module.paid.navigations

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import co.com.japl.module.paid.R
import java.time.YearMonth
import java.time.format.DateTimeFormatter

object Projections {

    fun listNavigate(navController: NavController){
        val request = NavDeepLinkRequest.Builder.fromUri(
            navController.context.getString(R.string.navigate_projection_list).toUri())
            .build()
        navController.navigate(request)
    }

    fun formNavigate(navController: NavController){
        val request = NavDeepLinkRequest.Builder.fromUri(
            navController.context.getString(R.string.navigate_projection_form).toUri())
            .build()
        navController.navigate(request)
    }

    fun formNavigate(id:Int,navController: NavController){
        val request = NavDeepLinkRequest.Builder.fromUri(
            navController.context.getString(R.string.navigate_projection_form_edit,id).toUri())
            .build()
        navController.navigate(request)
    }

}