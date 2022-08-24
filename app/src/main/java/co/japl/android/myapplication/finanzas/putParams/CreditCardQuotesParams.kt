package co.japl.android.myapplication.finanzas.putParams

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.utils.DateUtils
import java.time.LocalDateTime

class CreditCardQuotesParams {
    object Params{
        val PARAM_CREDIT_CARD_CODE = "CreditCardCode"
        val PARAM_CREDIT_CARD_NAME = "CreditCardName"
        val PARAM_CREDIT_CARD_CUTOFF = "CreditCardCutoff"
    }
    companion object {
        object Historical {
            @RequiresApi(Build.VERSION_CODES.O)
            fun newInstance(code: Int, cutOff: LocalDateTime, navController: NavController) {
                val parameter = bundleOf(
                    Params.PARAM_CREDIT_CARD_CODE to code,
                    Params.PARAM_CREDIT_CARD_CUTOFF to DateUtils.localDateTimeToString(cutOff)
                )
                navController.navigate(R.id.action_list_cc_quote_to_list_bought, parameter)
            }

            @RequiresApi(Build.VERSION_CODES.O)
            fun download(argument: Bundle): Pair<Int, LocalDateTime> {
                argument.let {
                    val code = it.get(Params.PARAM_CREDIT_CARD_CODE).toString().toInt()
                    val cutOff = DateUtils.toLocalDateTime(
                        it.get(Params.PARAM_CREDIT_CARD_CUTOFF).toString()
                    )
                    return Pair(code, cutOff)
                }
            }

            fun toBack(navController: NavController) {
                navController.navigate(R.id.action_list_bought_to_list_cc_quote)
            }
        }
        object CreateQuote {
            @RequiresApi(Build.VERSION_CODES.O)
            fun newInstance(code: Int, name: String, navController: NavController) {
                val parameter = bundleOf(
                    Params.PARAM_CREDIT_CARD_CODE to code,
                    Params.PARAM_CREDIT_CARD_NAME to name
                )
                navController.navigate(R.id.action_item_menu_side_boughtmade_to_buy_credit_card, parameter)
            }

            @RequiresApi(Build.VERSION_CODES.O)
            fun download(argument: Bundle): Pair<Int, String> {
                argument.let {
                    val code = it.get(Params.PARAM_CREDIT_CARD_CODE).toString().toInt()
                    val name = it.get(Params.PARAM_CREDIT_CARD_NAME).toString()
                    return Pair(code, name)
                }
            }

            fun toBack(navController: NavController) {
                navController.navigate(R.id.action_buy_credit_card_to_item_menu_side_boughtmade)
            }
        }
    }


}