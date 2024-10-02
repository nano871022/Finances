package co.com.japl.ui

import android.content.Context
import android.content.SharedPreferences

class Prefs constructor(private val context:Context){
    val PREFS_NAME = "co.japl.android.myapplication.finanzas"
    val SHARED_NAME_SIMULATOR = "shared_qcc_simulator"
    val SHARED_NAME_CREDIT_CARD_SMS_DAYS_READ = "credit_card_sms_days_read"
    val SHARED_NAME_PAID_SMS_DAYS_READ = "paid_sms_days_read"
    val SHARED_NAME_MSG_INITIAL = "msg_initial"
    val prefs:SharedPreferences = context.getSharedPreferences(PREFS_NAME,0)

    var simulator: Boolean
        get() = prefs.getString(SHARED_NAME_SIMULATOR,"false").toBoolean()
        set(value) = prefs.edit().putString(SHARED_NAME_SIMULATOR,value.toString()).apply()

    var creditCardSMSDaysRead: Int
        get() = prefs.getString(SHARED_NAME_CREDIT_CARD_SMS_DAYS_READ,"7")?.toInt()?:7
        set(value) = prefs.edit().putString(SHARED_NAME_CREDIT_CARD_SMS_DAYS_READ,value.toString()).apply()

    var paidSMSDaysRead: Int
        get() = prefs.getString(SHARED_NAME_PAID_SMS_DAYS_READ,"7")?.toInt()?:7
        set(value) = prefs.edit().putString(SHARED_NAME_PAID_SMS_DAYS_READ,value.toString()).apply()

    var msgInitial: Boolean
        get() = prefs.getString(SHARED_NAME_MSG_INITIAL,"true")?.toBoolean()?:true
        set(value) = prefs.edit().putString(SHARED_NAME_MSG_INITIAL,value.toString()).apply()

}