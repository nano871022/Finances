package co.japl.android.myapplication.finanzas.putParams

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.DTO.AdditionalCreditDTO
import co.com.japl.ui.utils.DateUtils
import com.google.gson.Gson
import androidx.core.net.toUri

class AdditionalCreditParams {
    object Params{
        const val PARAM_CREDIT_CODE = "CREDIT_CODE"
        const val PARAM_ID_ADDITIONAL_CREDIT = "id"
        const val PARAM_DEEPLINK = "android-support-nav:controller:deepLinkIntent"
    }

    companion object{

        @RequiresApi(Build.VERSION_CODES.O)
        fun download(argument:Bundle):Pair<Int,Int> {
            argument.let {
                if( it.containsKey(Params.PARAM_DEEPLINK) ){
                    val intent = it.get(Params.PARAM_DEEPLINK) as Intent
                    val codeCredit = intent.dataString?.toUri()?.getQueryParameter(Params.PARAM_CREDIT_CODE)?.let{
                        it.toInt()
                    }?:0
                    val id = intent.dataString?.toUri()?.getQueryParameter(Params.PARAM_ID_ADDITIONAL_CREDIT)?.let{
                        it.toInt()
                    }?:0
                    return Pair(id,codeCredit)
                }
            }
            return Pair(0,0)
        }

    }
}