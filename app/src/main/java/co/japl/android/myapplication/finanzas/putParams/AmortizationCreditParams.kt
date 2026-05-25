package co.japl.android.myapplication.finanzas.putParams

import android.content.Intent
import android.os.Bundle
import androidx.core.net.toUri
import androidx.navigation.NavController
import co.com.japl.ui.utils.DateUtils
import co.japl.android.myapplication.finanzas.putParams.AdditionalCreditParams.Params
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
                        "CODE" to (intent?.getQueryParameter("code")?.toLong()?:0.toLong()),
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