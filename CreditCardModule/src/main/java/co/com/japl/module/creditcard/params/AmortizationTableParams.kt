package co.com.japl.module.creditcard.params

import android.content.Intent
import android.os.Bundle
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.CalcDTO
import co.com.japl.ui.utils.DateUtils
import co.com.japl.module.creditcard.R


import co.com.japl.finances.iports.enums.AmortizationKindOfEnum
import co.com.japl.module.creditcard.params.PeriodsParams.Params
import com.google.gson.Gson
import java.time.LocalDate
import androidx.lifecycle.SavedStateHandle

class AmortizationTableParams {

    object params{
        val ARG_PARAM_CREDIT_VALUE = "credit_value"
        val ARG_PARAM_KIND_OF_AMORTIZATION = "kind_of_amortization"
    }

    companion object{
        fun newInstance(creditValue: CalcDTO, navController: NavController){
            val parameters = bundleOf(params.ARG_PARAM_CREDIT_VALUE to Gson().toJson(creditValue),params.ARG_PARAM_KIND_OF_AMORTIZATION to AmortizationKindOfEnum.EXTRA_VALUE_AMORTIZATION)
            navController.navigate(R.id.action_item_menu_side_listsave_to_amortizationTableFragment,parameters)
        }

        fun newInstanceQuotes(code:Long,navController:NavController){
            val parameters = bundleOf("CODE" to code)
            navController.navigate(R.id.action_list_bought_to_amortizationTableFragment,parameters)
        }

        fun download(argument: Bundle): Map<String, Any> {
            argument.let {
                if (it.containsKey(Params.PARAM_DEEPLINK)) {
                    val intent = (it.get(Params.PARAM_DEEPLINK) as Intent).dataString?.toUri()
                    val date = intent?.getQueryParameter("last_date")?.let { DateUtils.toLocalDate(it) }
                    return@download mapOf<String, Any>(
                        "CREDIT_CODE" to (intent?.getQueryParameter("credit_code")?.toLongOrNull() ?: 0L),
                        "CODE" to (intent?.getQueryParameter("code")?.toLongOrNull() ?: 0L),
                        "LAST_DATE" to (date ?: LocalDate.now())
                    )
                } else {
                    val rawCode = it.get("CODE") ?: it.get("code")
                    val code = when (rawCode) {
                        is Long -> rawCode
                        is Int -> rawCode.toLong()
                        is String -> rawCode.toLongOrNull() ?: 0L
                        else -> 0L
                    }
                    val rawCreditCode = it.get("CREDIT_CODE") ?: it.get("credit_code")
                    val creditCode = when (rawCreditCode) {
                        is Long -> rawCreditCode
                        is Int -> rawCreditCode.toLong()
                        is String -> rawCreditCode.toLongOrNull() ?: 0L
                        else -> 0L
                    }
                    return@download mapOf<String, Any>(
                        "CODE" to code,
                        "CREDIT_CODE" to creditCode
                    )
                }
            }
            return mapOf()
        }

        fun download(savedStateHandle: SavedStateHandle): Map<String, Any> {
            val rawCode = savedStateHandle.get<Any>("CODE")
                ?: savedStateHandle.get<Any>("code")
            val rawCreditCode = savedStateHandle.get<Any>("CREDIT_CODE")
                ?: savedStateHandle.get<Any>("credit_code")
            val rawLastDate = savedStateHandle.get<Any>("LAST_DATE")
                ?: savedStateHandle.get<Any>("last_date")

            val code = when (rawCode) {
                is Long -> rawCode
                is Int -> rawCode.toLong()
                is String -> rawCode.toLongOrNull()
                else -> null
            }
            val creditCode = when (rawCreditCode) {
                is Long -> rawCreditCode
                is Int -> rawCreditCode.toLong()
                is String -> rawCreditCode.toLongOrNull()
                else -> null
            }
            val lastDate = when (rawLastDate) {
                is LocalDate -> rawLastDate
                is String -> DateUtils.toLocalDate(rawLastDate)
                else -> null
            }

            if (code != null || creditCode != null) {
                return mapOf<String, Any>(
                    "CODE" to (code ?: 0L),
                    "CREDIT_CODE" to (creditCode ?: 0L),
                    "LAST_DATE" to (lastDate ?: LocalDate.now())
                )
            }

            savedStateHandle.get<Intent>(Params.PARAM_DEEPLINK)?.let { intent ->
                val uri = intent.dataString?.toUri()
                val date = uri?.getQueryParameter("last_date")?.let { DateUtils.toLocalDate(it) }
                return mapOf<String, Any>(
                    "CREDIT_CODE" to (uri?.getQueryParameter("credit_code")?.toLongOrNull() ?: 0L),
                    "CODE" to (uri?.getQueryParameter("code")?.toLongOrNull() ?: 0L),
                    "LAST_DATE" to (date ?: LocalDate.now())
                )
            }
            return mapOf()
        }

        fun toBack(navController: NavController){
            navController.popBackStack()
        }
    }

}