package co.com.japl.ui

import android.content.Context
import android.content.SharedPreferences

class Prefs constructor(private val context:Context){
    val PREFS_NAME = "co.japl.android.myapplication.finanzas"
    val SHARED_NAME = "shared_qcc_simulator"
    val prefs:SharedPreferences = context.getSharedPreferences(PREFS_NAME,0)

    var simulator: Boolean
        get() = prefs.getString(SHARED_NAME,"false").toBoolean()
        set(value) = prefs.edit().putString(SHARED_NAME,value.toString()).apply()
}