package co.com.japl.finanzas.module.inputs.navigates

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import co.com.japl.finanzas.module.inputs.R

object Input {

    fun navigate(codeAccount:Int,navController: NavController){
        val request = NavDeepLinkRequest.Builder.fromUri(navController.context.getString(R.string.navigate_input,codeAccount).toUri()).build()
        navController.navigate(request)
    }
}