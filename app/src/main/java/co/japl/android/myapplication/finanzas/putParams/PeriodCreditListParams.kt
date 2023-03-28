package co.japl.android.myapplication.finanzas.putParams

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.utils.DateUtils
import java.time.LocalDate

class PeriodCreditListParams {
    object Params{
        const val PARAM_DATE_BILL = "DATE_BILL"
    }
    companion object{
        @RequiresApi(Build.VERSION_CODES.O)
        fun newInstance(dateBill: LocalDate, navController: NavController){
            val parameters = bundleOf( CreditFixListParams.Params.PARAM_DATE_BILL to DateUtils.localDateToString(dateBill))
            navController.navigate(R.id.action_periodCreditListFragment_to_monthlyCreditListFragment,parameters)
        }
    }
}