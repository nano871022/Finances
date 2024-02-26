package co.japl.android.myapplication.finanzas.putParams

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import co.japl.android.myapplication.R
import co.com.japl.ui.utils.DateUtils
import java.time.LocalDateTime

class PeriodsQuotesParams {
    object Params{
        val PARAM_CREDIT_CARD_CODE = "CreditCardCode"
        val PARAM_CREDIT_CARD_NAME = "CreditCardName"
        val PARAM_CREDIT_CARD_CUTOFF = "CreditCardCutoff"
        val PARAM_CREDIT_CARD_CUTOFF_DAY = "CreditCardCutOffDay"
    }
    companion object {
        object Historical {
            @RequiresApi(Build.VERSION_CODES.O)
            fun newInstance(code: Int,cutOffDay:Short, cutOff: LocalDateTime, navController: NavController) {
                val parameter = bundleOf(
                    Params.PARAM_CREDIT_CARD_CODE to code,
                    Params.PARAM_CREDIT_CARD_CUTOFF to DateUtils.localDateTimeToString(cutOff),
                    Params.PARAM_CREDIT_CARD_CUTOFF_DAY to cutOffDay
                )
                navController.navigate(R.id.action_list_periods_to_list_bought, parameter)
            }

            @RequiresApi(Build.VERSION_CODES.O)
            fun download(argument: Bundle): Triple<Int, LocalDateTime,Short> {
                argument.let {
                    val code = it.get(Params.PARAM_CREDIT_CARD_CODE).toString().toInt()
                    val cutOff = DateUtils.toLocalDateTime(
                        it.get(Params.PARAM_CREDIT_CARD_CUTOFF).toString()
                    )
                    val cutOffDay = it.get(Params.PARAM_CREDIT_CARD_CUTOFF_DAY).toString().toShort()
                    return Triple(code, cutOff,cutOffDay)
                }
            }

            fun toBack(navController: NavController) {
                navController.popBackStack()
            }
        }

    }


}