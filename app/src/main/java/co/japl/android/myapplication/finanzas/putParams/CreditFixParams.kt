package co.japl.android.myapplication.finanzas.putParams

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.DTO.CreditDTO
import co.japl.android.myapplication.utils.DateUtils
import com.google.gson.Gson

class CreditFixParams {
    object Params{
        const val PARAMS_CREDIT_CODE = "CREDIT_CODE"
        const val PARAMS_CREDIT = "CREDIT"
        const val PARAMS_DATE_BILL = "DATE_BILL"
    }

    companion object{
        fun newInstanceAdditionalList(creditCode:Long,navController: NavController){
            val arguments = bundleOf( Params.PARAMS_CREDIT_CODE to creditCode)
            navController.navigate(R.id.action_creditFixFragment_to_additionalListFragment,arguments)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun newInstanceAmortizationList(credit:CreditDTO, navController: NavController){
            val parameters = bundleOf( Params.PARAMS_CREDIT to Gson().toJson(credit),Params.PARAMS_DATE_BILL to DateUtils.localDateToString(credit.date))
            navController.navigate(R.id.action_creditFixFragment_to_amortizationCreditFragment,parameters)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun newInstanceAmortizationMonthlyList(credit:CreditDTO, navController: NavController){
            val parameters = bundleOf( Params.PARAMS_CREDIT to Gson().toJson(credit),Params.PARAMS_DATE_BILL to DateUtils.localDateToString(credit.date))
            navController.navigate(R.id.action_monthlyCreditListFragment_to_amortizationCreditFragment,parameters)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun downloadAmortizationList(arguments:Bundle):CreditDTO{
            val date = DateUtils.toLocalDate(arguments.get(Params.PARAMS_DATE_BILL) as String)
            val value = Gson().fromJson(arguments.get(Params.PARAMS_CREDIT) as String,CreditDTO::class.java)
            value.date = date
            return value
        }

        fun downloadAdditionalList(arguments:Bundle):Long{
            return (arguments.get(Params.PARAMS_CREDIT_CODE) as Long).also{
                Log.d(javaClass.name,"Credit Code: $it")
            }
        }

        fun toBack(navController: NavController){
            navController.popBackStack()
        }
    }

}