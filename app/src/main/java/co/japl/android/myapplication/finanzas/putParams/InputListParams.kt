package co.japl.android.myapplication.finanzas.putParams

import androidx.navigation.NavController
import co.japl.android.myapplication.R

class InputListParams {
    object PARAMS{

    }
    companion object {

        fun newInstance(navController: NavController) {
            navController.navigate(R.id.action_inputListFragment_to_inputFragment)
        }

        fun download(navController: NavController) {

        }

        fun toBack(navController: NavController){
            navController.popBackStack()
        }

    }
}