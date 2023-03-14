package co.japl.android.myapplication.finanzas.putParams

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import co.japl.android.myapplication.R
import java.util.*

class BoughWalletParams {

    object params{
        val ARG_PARAM_CODE_CREDIT_CARD = "cod_credit_card"
    }

    companion object{
        fun newInstance(code:Int,navController: NavController){
            val parameters = bundleOf(params.ARG_PARAM_CODE_CREDIT_CARD to code.toString())
            navController.navigate(R.id.action_item_menu_side_boughtmade_to_boughWalletController,parameters)
        }
        fun download(argument:Bundle):String{
            argument.let {
                return it.get(params.ARG_PARAM_CODE_CREDIT_CARD).toString()
            }
        }
        fun toBack(navController: NavController){
            navController.popBackStack()
        }
    }
}