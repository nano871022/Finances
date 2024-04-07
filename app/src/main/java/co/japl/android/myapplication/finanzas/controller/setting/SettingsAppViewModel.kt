package co.japl.android.myapplication.finanzas.controller.setting

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import co.com.japl.ui.Prefs
import co.japl.android.myapplication.R

class SettingsAppViewModel constructor(private val prefs:Prefs,private val context:Context) {

    val msgInitial = mutableStateOf(prefs.msgInitial)
    fun save(){
        prefs.msgInitial = msgInitial.value
        Toast.makeText(context, R.string.toast_save_successful,Toast.LENGTH_SHORT).show()
    }

}