package co.com.japl.ui

import android.content.Context
import android.content.SharedPreferences

class Prefs(private val context:Context){
    val PREFS_NAME = "co.japl.android.myapplication.finanzas"
    val SHARED_NAME_SIMULATOR = "shared_qcc_simulator"
    val SHARED_NAME_CREDIT_CARD_SMS_DAYS_READ = "credit_card_sms_days_read"
    val SHARED_NAME_CREDIT_CARD_EMAIL_DAYS_READ = "credit_card_email_days_read"
    val SHARED_NAME_PAID_EMAIL_DAYS_READ = "paid_email_days_read"
    val SHARED_NAME_PAID_SMS_DAYS_READ = "paid_sms_days_read"
    val SHARED_NAME_MSG_INITIAL = "msg_initial"
    val SHARED_NAME_LLM_TYPE = "llm_type"
    val SHARED_NAME_LLM_API_KEY = "llm_api_key"
    val SHARED_NAME_LLM_ENABLED = "llm_enabled"
    val SHARED_NAME_LLM_MODEL = "llm_model"
    val SHARED_NAME_LLM_GEMINI_URL = "llm_gemini_url"
    val SHARED_NAME_LLM_DEEPSEEK_URL = "llm_deepseek_url"
    val prefs:SharedPreferences = context.getSharedPreferences(PREFS_NAME,0)

    var llmType: String
        get() = prefs.getString(SHARED_NAME_LLM_TYPE, "GEMINI") ?: "GEMINI"
        set(value) = prefs.edit().putString(SHARED_NAME_LLM_TYPE, value).apply()

    var llmApiKey: String
        get() = prefs.getString(SHARED_NAME_LLM_API_KEY, "") ?: ""
        set(value) = prefs.edit().putString(SHARED_NAME_LLM_API_KEY, value).apply()

    var llmEnabled: Boolean
        get() = prefs.getBoolean(SHARED_NAME_LLM_ENABLED, false)
        set(value) = prefs.edit().putBoolean(SHARED_NAME_LLM_ENABLED, value).apply()

    var llmModel: String
        get() = prefs.getString(SHARED_NAME_LLM_MODEL, "") ?: ""
        set(value) = prefs.edit().putString(SHARED_NAME_LLM_MODEL, value).apply()

    var llmGeminiUrl: String
        get() = prefs.getString(SHARED_NAME_LLM_GEMINI_URL, "https://generativelanguage.googleapis.com/v1beta/models/") ?: "https://generativelanguage.googleapis.com/v1beta/models/"
        set(value) = prefs.edit().putString(SHARED_NAME_LLM_GEMINI_URL, value).apply()

    var llmDeepSeekUrl: String
        get() = prefs.getString(SHARED_NAME_LLM_DEEPSEEK_URL, "https://api.deepseek.com/v1/") ?: "https://api.deepseek.com/v1/"
        set(value) = prefs.edit().putString(SHARED_NAME_LLM_DEEPSEEK_URL, value).apply()

    var simulator: Boolean
        get() = prefs.getString(SHARED_NAME_SIMULATOR,"false").toBoolean()
        set(value) = prefs.edit().putString(SHARED_NAME_SIMULATOR,value.toString()).apply()

    var creditCardSMSDaysRead: Int
        get() = prefs.getString(SHARED_NAME_CREDIT_CARD_SMS_DAYS_READ,"7")?.toInt()?:7
        set(value) = prefs.edit().putString(SHARED_NAME_CREDIT_CARD_SMS_DAYS_READ,value.toString()).apply()

    var creditCardEmailDaysRead: Int
        get() = prefs.getString(SHARED_NAME_CREDIT_CARD_EMAIL_DAYS_READ,"7")?.toInt()?:7
        set(value) = prefs.edit().putString(SHARED_NAME_CREDIT_CARD_EMAIL_DAYS_READ,value.toString()).apply()

    var paidSMSDaysRead: Int
        get() = prefs.getString(SHARED_NAME_PAID_SMS_DAYS_READ,"7")?.toInt()?:7
        set(value) = prefs.edit().putString(SHARED_NAME_PAID_SMS_DAYS_READ,value.toString()).apply()

    var paidEmailDaysRead: Int
        get() = prefs.getString(SHARED_NAME_PAID_EMAIL_DAYS_READ,"7")?.toInt()?:7
        set(value) = prefs.edit().putString(SHARED_NAME_PAID_EMAIL_DAYS_READ,value.toString()).apply()

    var msgInitial: Boolean
        get() = prefs.getString(SHARED_NAME_MSG_INITIAL,"true")?.toBoolean()?:true
        set(value) = prefs.edit().putString(SHARED_NAME_MSG_INITIAL,value.toString()).apply()

}
