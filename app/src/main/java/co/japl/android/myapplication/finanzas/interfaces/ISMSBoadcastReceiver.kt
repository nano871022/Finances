package co.japl.android.myapplication.finanzas.interfaces

import android.content.Context
import android.content.Intent

interface ISMSBoadcastReceiver {

    fun onReceive(context: Context?, intent: Intent?)
}