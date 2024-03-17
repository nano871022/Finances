package co.com.japl.ui

import android.content.Context
import android.content.SharedPreferences

class Prefs constructor(private val context:Context){
    val PREFS_NAME = "co.japl.android.myapplication.finanzas"
    val SHARED_NAME_SIMULATOR = "shared_qcc_simulator"
    val SHARED_SMS_SENDER_TC = "shared_sms_sender_tc"
    val SHARED_SMS_BODY_REGEX_TC = "shared_sms_body_regex_tc"
    val prefs:SharedPreferences = context.getSharedPreferences(PREFS_NAME,0)

    var simulator: Boolean
        get() = prefs.getString(SHARED_NAME_SIMULATOR,"false").toBoolean()
        set(value) = prefs.edit().putString(SHARED_NAME_SIMULATOR,value.toString()).apply()

    var smsSender: String?
        get() = prefs.getString(SHARED_SMS_SENDER_TC,"87400")
        set(value) = prefs.edit().putString(SHARED_SMS_SENDER_TC,value.toString()).apply()

    var smsBodyRegex: String?
        get() = prefs.getString(SHARED_SMS_BODY_REGEX_TC,"\\W+ $(\\d+\\.\\d+) en (\\W+)\\. (\\d{2}\\/\\d{2}\\/\\d{4}) T.Cred")
        set(value) = prefs.edit().putString(SHARED_SMS_BODY_REGEX_TC,value.toString()).apply()
}