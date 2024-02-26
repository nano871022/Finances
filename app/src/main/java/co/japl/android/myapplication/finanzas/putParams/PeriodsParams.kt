package co.japl.android.myapplication.finanzas.putParams

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import co.japl.android.myapplication.R

class PeriodsParams {
    object Params{
        val PARAM_CREDIT_CARD_CODE = "CreditCardCode"
        val PARAM_CODE_CREDIT_CARD = "codeCreditCard"
        val PARAM_CUTOFF_DAY = "cutOffDay"
        val PARAM_CUTOFF = "cutOff"
        val PARAM_DEEPLINK = "android-support-nav:controller:deepLinkIntent"
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
                Log.d(javaClass.name,"=== download $argument")
                argument.let {
                    if(it.containsKey(Params.PARAM_CREDIT_CARD_CODE)) {
                        return it.get(Params.PARAM_CREDIT_CARD_CODE).toString().toInt()
                    }
                    if(it.containsKey(Params.PARAM_DEEPLINK)) {
                        val intent = it.get(Params.PARAM_DEEPLINK) as Intent
                        Uri.parse(intent.dataString).getQueryParameters(Params.PARAM_CODE_CREDIT_CARD).let {
                            return it[0]!!.toInt()
                        }
                    }
                }
                return 0
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