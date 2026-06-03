package co.com.japl.module.credit.params

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import co.com.japl.module.credit.R
import co.com.japl.ui.utils.DateUtils
import java.time.LocalDate
import java.time.YearMonth

class CreditFixListParams {
    object Params{
        const val PARAM_DATE_BILL = "DATE_BILL"
        const val PARAM_DEEPLINK = "android-support-nav:controller:deepLinkIntent"
    }
    companion object{
        fun newInstance(navController: NavController){
            navController.navigate(R.id.action_creditListFragment_to_creditFixFragment)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun newInstanceMonthly(dateBill:LocalDate, navController: NavController){
            val parameters = bundleOf( Params.PARAM_DATE_BILL to DateUtils.localDateToString(dateBill))
            navController.navigate(R.id.action_creditListFragment_to_monthlyCreditListFragment,parameters)
        }

        fun newInstancePeriod(navController: NavController){
            navController.navigate(R.id.action_creditListFragment_to_periodCreditListFragment)
        }

        fun newInstanceAdditionalList(creditCode:Long,navController: NavController){
            val arguments = bundleOf( CreditFixParams.Params.PARAMS_CREDIT_CODE to creditCode)
            navController.navigate(R.id.action_monthlyCreditListFragment_to_additionalListFragment,arguments)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun downloadMonthly(arguments:Bundle?):LocalDate?{
            return arguments?.let{
               DateUtils.toLocalDate(it.get(Params.PARAM_DATE_BILL) as String)
            }
        }

        fun toBack(navController: NavController){
            navController.popBackStack()
        }

        fun downloadListCreditFix(savedStateHandle: SavedStateHandle):Map<String,Any>{
            val code = savedStateHandle.get<Long>("CODE")
            val creditCode = savedStateHandle.get<Long>("CREDIT_CODE")
            val lastDate = savedStateHandle.get<LocalDate>("LAST_DATE")

            if(code != null || creditCode != null){
                return mapOf<String,Any>(
                    "CODE" to (code ?: 0L),
                    "CREDIT_CODE" to (creditCode ?: 0L),
                    "LAST_DATE" to (lastDate ?: LocalDate.now())
                )
            }

            savedStateHandle.get<Intent>(Params.PARAM_DEEPLINK)?.let { intent ->
                val uri = intent.dataString?.toUri()
                return mapOf<String,Any>(
                    "YEAR_MONTH" to (uri?.getQueryParameter("year_month")?.toLongOrNull()?: YearMonth.now())
                )
            }
            return mapOf()
        }

    }
}