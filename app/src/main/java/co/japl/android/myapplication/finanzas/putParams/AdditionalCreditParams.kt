package co.japl.android.myapplication.finanzas.putParams

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.DTO.AdditionalCreditDTO
import co.japl.android.myapplication.utils.DateUtils
import com.google.gson.Gson

class AdditionalCreditParams {
    object Params{
        const val PARAMS_ADDITIONAL = "ADDITIONAL_CREDIT_PARAM"
        const val PARAMS_START_DATE = "START_DATE"
        const val PARAMS_END_DATE = "END_DATE"
    }

    companion object{
        @RequiresApi(Build.VERSION_CODES.O)
        fun newInstance(additional:AdditionalCreditDTO, navController: NavController){
            val parameters = bundleOf(Params.PARAMS_ADDITIONAL to Gson().toJson(additional)
                ,Params.PARAMS_START_DATE to DateUtils.localDateToString(additional.startDate)
                ,Params.PARAMS_END_DATE to DateUtils.localDateToString(additional.endDate))
            navController.navigate(R.id.action_additionalListFragment_to_additionalCreditFragment,parameters)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun download(argument:Bundle):AdditionalCreditDTO? {
            return argument.let {
                val start = it.get(Params.PARAMS_START_DATE) as String
                val end = it.get(Params.PARAMS_END_DATE) as String
                Log.d(javaClass.name,"download $start $end")
                val value = Gson().fromJson(it.get(Params.PARAMS_ADDITIONAL) as String,AdditionalCreditDTO::class.java)
                value.startDate = DateUtils.toLocalDate(start)
                value.endDate = DateUtils.toLocalDate(end)
                return value
            }
        }


        fun toBack(navController: NavController) = navController.popBackStack()
    }
}