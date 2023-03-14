package co.japl.android.myapplication.finanzas.putParams

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.utils.DateUtils
import java.time.LocalDateTime

class PeriodsParams {
    object Params{
        val PARAM_CREDIT_CARD_CODE = "CreditCardCode"
    }
    companion object {
        object Historical {
            @RequiresApi(Build.VERSION_CODES.O)
            fun newInstance(code: Int, navController: NavController) {
                val parameter = bundleOf(
                    Params.PARAM_CREDIT_CARD_CODE to code,
                )
                navController.navigate(R.id.action_item_menu_side_boughtmade_to_list_periods, parameter)
            }

            @RequiresApi(Build.VERSION_CODES.O)
            fun download(argument: Bundle): Int {
                argument.let {
                   return it.get(Params.PARAM_CREDIT_CARD_CODE).toString().toInt()
                }
            }

            fun toBack(navController: NavController) {
                navController.popBackStack()
            }
        }
        object CreatePeriods {
            @RequiresApi(Build.VERSION_CODES.O)
            fun newInstance(code: Int, navController: NavController) {
                val parameter = bundleOf(
                    Params.PARAM_CREDIT_CARD_CODE to code,
                )
                navController.navigate(R.id.action_item_menu_side_boughtmade_to_list_periods, parameter)
            }

            @RequiresApi(Build.VERSION_CODES.O)
            fun download(argument: Bundle): Int {
                argument.let {
                    return it.get(Params.PARAM_CREDIT_CARD_CODE).toString().toInt()
                }
            }

            fun toBack(navController: NavController) {
                navController.popBackStack()
            }
        }
    }


}