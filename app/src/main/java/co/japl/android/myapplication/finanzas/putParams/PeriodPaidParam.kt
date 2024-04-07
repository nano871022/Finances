package co.japl.android.myapplication.finanzas.putParams

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi

class PeriodPaidParam {
    object Params{
        val PARAM_CODE_ACCOUNT = "code_account"
        const val ARG_DEEPLINK = "android-support-nav:controller:deepLinkIntent"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun downloadList(parameters: Bundle): Int? {
        if (parameters.containsKey(PeriodPaidParam.Params.ARG_DEEPLINK)) {
            val intent = parameters.get(PeriodPaidParam.Params.ARG_DEEPLINK) as Intent
            if (Uri.parse(intent.dataString)
                    .getQueryParameter(PeriodPaidParam.Params.PARAM_CODE_ACCOUNT) != null
            ) {
                return Uri.parse(intent.dataString)
                    .getQueryParameter(PeriodPaidParam.Params.PARAM_CODE_ACCOUNT)?.toInt()
            }
        }
        return null
    }
}