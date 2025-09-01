package co.japl.android.myapplication.finanzas.putParams

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import co.com.japl.ui.utils.DateUtils
import co.japl.android.myapplication.R
import co.japl.android.myapplication.bussiness.DTO.CalcDTO
import co.japl.android.myapplication.bussiness.interfaces.Calc
import co.japl.android.myapplication.finanzas.enums.AmortizationKindOfEnum
import co.japl.android.myapplication.finanzas.putParams.AdditionalCreditParams.Params
import com.google.gson.Gson
import java.net.URLDecoder
import java.nio.charset.Charset
import java.time.LocalDate

class AmortizationCreditParams {

    companion object{
        fun download(argument: Bundle):Map<String,Any>{
            argument.let {
                if( it.containsKey(Params.PARAM_DEEPLINK) ){
                    val intent = (it.get(Params.PARAM_DEEPLINK) as Intent).dataString?.toUri()
                    val date = intent?.getQueryParameter("last_date")?.let{ DateUtils.toLocalDate(it) }
                    return@download mapOf<String,Any>(
                        "CREDIT_CODE" to (intent?.getQueryParameter("credit_code")?.toLong()?:0.toLong()),
                        "LAST_DATE" to (date?: LocalDate.now())
                    )
                }else{
                    val code = it.getLong("CODE")
                    return@download mapOf<String,Any>(
                        "CODE" to code
                    )
                }
            }
            return mapOf()
        }

        fun toBack(navController: NavController){
            navController.popBackStack()
        }
    }

}