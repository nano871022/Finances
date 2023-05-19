package co.japl.android.myapplication.finanzas.putParams

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import co.japl.android.myapplication.R

class InputListParams {
    object PARAMS{
        const val PARAM_ACCOUNT_CODE = "account_code"
    }
    companion object {

        fun newInstance(accountCode:Int,navController: NavController) {
            val parameters = bundleOf(PARAMS.PARAM_ACCOUNT_CODE to accountCode)
            navController.navigate(R.id.action_inputListFragment_to_inputFragment,parameters)
        }

        fun download(parameters: Bundle):Int = parameters.getInt(PARAMS.PARAM_ACCOUNT_CODE)

        fun toBack(navController: NavController)=  navController.popBackStack()
    }
}