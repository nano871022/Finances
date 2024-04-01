package co.japl.android.myapplication.finanzas.putParams

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidDTO
import co.com.japl.ui.utils.DateUtils
import java.time.LocalDate

class PaidsParams {
    object Params{
        val PARAM_DATE_PERIOD = "date_period"
        val PARAM_CODE_ACCOUNT = "code_account"
        val PARAM_CODE_PAID = "code_paid"
        const val ARG_DEEPLINK = "android-support-nav:controller:deepLinkIntent"
    }

    companion object{

        fun newInstance(navController: NavController){
            navController.navigate(R.id.action_paidsFragment_to_paidFragment)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun newInstanceDetail(date:LocalDate, navController: NavController){
            val parameter = bundleOf(Params.PARAM_DATE_PERIOD to DateUtils.localDateToString(date))
            navController.navigate(R.id.action_periodsPaidFragment_to_paidListFragment,parameter)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun downloadList(parameters:Bundle):LocalDate?{
            if(parameters.containsKey(Params.ARG_DEEPLINK)){
                val intent = parameters.get(Params.ARG_DEEPLINK) as Intent
                if(Uri.parse(intent.dataString).getQueryParameter(Params.PARAM_DATE_PERIOD) != null){
                    return Uri.parse(intent.dataString).getQueryParameter(Params.PARAM_DATE_PERIOD)?.let{
                        DateUtils.toLocalDate(it)
                    }
                }
            }

            return parameters.getString(Params.PARAM_DATE_PERIOD)?.let{
                DateUtils.toLocalDate(it)
            }
        }

        fun downloadCodeAccount(parameters:Bundle):Int?{
            if(parameters.containsKey(Params.ARG_DEEPLINK)){
                val intent = parameters.get(Params.ARG_DEEPLINK) as Intent
                if(Uri.parse(intent.dataString).getQueryParameter(Params.PARAM_CODE_ACCOUNT) != null){
                    return Uri.parse(intent.dataString).getQueryParameter(Params.PARAM_CODE_ACCOUNT)?.toInt()
                }
            }
            return null
        }

        fun downloadCodePaid(parameters:Bundle):Int?{
            if(parameters.containsKey(Params.ARG_DEEPLINK)){
                val intent = parameters.get(Params.ARG_DEEPLINK) as Intent
                if(Uri.parse(intent.dataString).getQueryParameter(Params.PARAM_CODE_PAID) != null){
                    return Uri.parse(intent.dataString).getQueryParameter(Params.PARAM_CODE_PAID)?.toInt()
                }
            }
            return null
        }

        fun download(parameters: Bundle):PaidDTO?{
            return null
        }

        fun toBack(navController: NavController){
            navController.popBackStack()
        }

    }
}