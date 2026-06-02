package co.com.japl.module.credit.params

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.net.toUri
import androidx.navigation.NavController
import co.com.japl.ui.utils.DateUtils
import co.com.japl.module.credit.params.AdditionalCreditParams.Params
import java.time.LocalDate
import androidx.lifecycle.SavedStateHandle

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

        fun download(savedStateHandle: SavedStateHandle):Map<String,Any>{
            val code = savedStateHandle.get<Long>("CODE")
            val creditCode = savedStateHandle.get<Long>("CREDIT_CODE")
            val lastDate = savedStateHandle.get<LocalDate>("LAST_DATE")

            if(code != null || creditCode != null){
                return mapOf<String,Any>(
                    "CODE" to (code ?: 0L),
                    "CREDIT_CODE" to (creditCode ?: 0L),
                    "LAST_DATE" to (lastDate ?: LocalDate.now())
                )
            }

            savedStateHandle.get<Intent>(Params.PARAM_DEEPLINK)?.let { intent ->
                 val uri = intent.dataString?.toUri()
                 val date = uri?.getQueryParameter("last_date")?.let{ DateUtils.toLocalDate(it) }
                 return mapOf<String,Any>(
                        "CREDIT_CODE" to (uri?.getQueryParameter("credit_code")?.toLongOrNull()?:0.toLong()),
                        "CODE" to (uri?.getQueryParameter("code")?.toLongOrNull()?:0.toLong()),
                        "LAST_DATE" to (date?: LocalDate.now())
                    )
            }
            return mapOf()
        }

        fun toBack(navController: NavController){
            navController.popBackStack()
        }
    }

}