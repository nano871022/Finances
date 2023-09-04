package co.japl.android.myapplication.finanzas.putParams

import android.R.bool
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
        const val PARAMS_VIEW = "VIEW"
    }

    companion object{
        @RequiresApi(Build.VERSION_CODES.O)
        fun newInstance(additional:AdditionalCreditDTO,view:Boolean, navController: NavController){
            val parameters = bundleOf(Params.PARAMS_ADDITIONAL to Gson().toJson(additional)
                ,Params.PARAMS_START_DATE to DateUtils.localDateToString(additional.startDate)
                ,Params.PARAMS_END_DATE to DateUtils.localDateToString(additional.endDate)
                ,Params.PARAMS_VIEW to view)
            navController.navigate(R.id.action_additionalListFragment_to_additionalCreditFragment,parameters)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun download(argument:Bundle):Pair<AdditionalCreditDTO?,Boolean> {
            return argument.let {
                val value = Gson().fromJson(it.getString(Params.PARAMS_ADDITIONAL),AdditionalCreditDTO::class.java)
                it.getString(Params.PARAMS_START_DATE)?.let{value.startDate = DateUtils.toLocalDate(it)}
                it.getString(Params.PARAMS_END_DATE)?.let{value.endDate = DateUtils.toLocalDate(it)}
                val view = it.getBoolean(Params.PARAMS_VIEW)
                return Pair(value,view)
            }
        }


        fun toBack(navController: NavController) = navController.popBackStack()
    }
}