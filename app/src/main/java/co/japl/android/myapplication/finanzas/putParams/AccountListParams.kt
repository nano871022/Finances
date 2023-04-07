package co.japl.android.myapplication.finanzas.putParams

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import co.japl.android.myapplication.R

class AccountListParams {
    object PARAMS{
        val PARAMS_ACCOUNT_ID = "account_id"
    }
    companion object {

        fun newInstance(navController: NavController) {
            navController.navigate(R.id.action_accountListFragment_to_accountFragment)
        }

        fun newInstance(id:Int,navController: NavController) {
            val parameters = bundleOf(PARAMS.PARAMS_ACCOUNT_ID to id.toString())
            navController.navigate(R.id.action_accountListFragment_to_accountFragment,parameters)
        }

        fun download(parameters: Bundle):Int = parameters.getString(PARAMS.PARAMS_ACCOUNT_ID)?.toInt()?:0

        fun toBack(navController: NavController)=navController.popBackStack()

    }
}