package co.com.japl.module.credit.navigations

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import co.com.japl.module.credit.R

object AdditionalList {

    fun navigateForm(codeCredit:Int,navController: NavController){
        val uri = navController.context.getString(R.string.navigate_add_additional,codeCredit).toUri()
        val request = NavDeepLinkRequest.Builder.fromUri(uri).build()
        navController.navigate(request)
    }
    fun navigateForm(id:Int,codeCredit:Int,navController: NavController){
        val request = NavDeepLinkRequest.Builder.fromUri(navController.context.getString(R.string.navigate_edit_additional,id,codeCredit).toUri()).build()
        navController.navigate(request)
    }

}