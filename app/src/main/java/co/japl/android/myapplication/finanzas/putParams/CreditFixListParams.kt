package co.japl.android.myapplication.finanzas.putParams

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.utils.DateUtils
import java.time.LocalDate

class CreditFixListParams {
    object Params{
        const val PARAM_DATE_BILL = "DATE_BILL"
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

        @RequiresApi(Build.VERSION_CODES.O)
        fun downloadMonthly(arguments:Bundle?):LocalDate?{
            return arguments?.let{
               DateUtils.toLocalDate(it.get(Params.PARAM_DATE_BILL) as String)
            }
        }

        fun toBack(navController: NavController){
            navController.popBackStack()
        }

    }
}