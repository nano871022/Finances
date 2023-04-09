package co.japl.android.myapplication.finanzas.putParams

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import co.japl.android.myapplication.R

class AccountParams {
    object PARAMS{
      val PARAM_ID_ACCOUNT = "id_account"
    }
    companion object {

        fun newInstance(id:Int,navController: NavController) {
            val parameters = bundleOf( PARAMS.PARAM_ID_ACCOUNT to id.toString())
            navController.navigate(R.id.action_accountFragment_to_inputListFragment,parameters)
        }

        fun download(parameter:Bundle):Int {
            return (parameter.get(PARAMS.PARAM_ID_ACCOUNT) as String).toInt()?:0

        }

        fun toBack(navController: NavController){
            navController.popBackStack()
        }

    }
}