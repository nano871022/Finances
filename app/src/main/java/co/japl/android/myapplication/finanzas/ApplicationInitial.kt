package co.japl.android.myapplication.finanzas

import android.app.Application
import co.com.japl.ui.Prefs
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationInitial:Application() {
    companion object {
        lateinit var prefs : Prefs
    }

    override fun onCreate() {
        super.onCreate()
        prefs = Prefs(applicationContext)
    }
}