package co.japl.android.myapplication.finanzas.putParams

import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import co.japl.android.myapplication.R

class AccountParams {
    object PARAMS{
     const val PARAM_ID_ACCOUNT = "id_account"
    }
    companion object {

        fun newInstance(id:Int,navController: NavController) {
            Log.d(javaClass.name,"Account ID: $id")
            val parameters = bundleOf( PARAMS.PARAM_ID_ACCOUNT to id)
            navController.navigate(R.id.action_accountFragment_to_inputListFragment,parameters)
        }

        fun newInstance(navController: NavController){
            navController.navigate(R.id.action_menu_item_paids_to_accountFragment)
        }

        fun download(parameter:Bundle):Int= parameter.getInt(PARAMS.PARAM_ID_ACCOUNT)

        fun toBack(navController: NavController)= navController.popBackStack()
    }
}