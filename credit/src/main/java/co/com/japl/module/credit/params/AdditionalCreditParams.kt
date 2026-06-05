package co.com.japl.module.credit.params

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle

class AdditionalCreditParams {
    object Params{
        const val PARAM_CREDIT_CODE = "CREDIT_CODE"
        const val PARAM_ID_ADDITIONAL_CREDIT = "id"
        const val PARAM_DEEPLINK = "android-support-nav:controller:deepLinkIntent"
    }

    companion object{

        @RequiresApi(Build.VERSION_CODES.O)
        fun download(argument: SavedStateHandle):Pair<Int,Int> {
            argument.let {
                argument.get<Intent>(Params.PARAM_DEEPLINK)?.let{ intent->
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