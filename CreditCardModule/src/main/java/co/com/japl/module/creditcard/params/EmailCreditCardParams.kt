package co.com.japl.module.creditcard.params

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.net.toUri


object EmailCreditCardParams {
    const val PARAM_DEEPLINK = "android-support-nav:controller:deepLinkIntent"
    const val CODE_EMAIL_CC = "code_email_credit_card"

    fun download(bundle: Bundle): Int {
        if(bundle.containsKey(PARAM_DEEPLINK)) {
            val intent = bundle.get(PARAM_DEEPLINK) as Intent
            val value = intent.dataString?.toUri()?.getQueryParameter(CODE_EMAIL_CC)
            return value?.toInt()?:0
        }
        return bundle.getInt(CODE_EMAIL_CC, 0)
    }

    fun upload(bundle: Bundle, code: Int) {
        bundle.putInt(CODE_EMAIL_CC, code)
    }
}
