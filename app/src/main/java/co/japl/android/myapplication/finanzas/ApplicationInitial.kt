package co.japl.android.myapplication.finanzas

import android.app.Application
import android.util.Log
import co.com.japl.ui.Prefs
import com.google.android.gms.security.ProviderInstaller
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationInitial:Application() {
    companion object {
        lateinit var prefs : Prefs
    }

    override fun onCreate() {
        super.onCreate()
        prefs = Prefs(applicationContext)
        
        // Update security provider to avoid TLS/Network issues with GmsNetworkStack
        try {
            ProviderInstaller.installIfNeededAsync(this, object : ProviderInstaller.ProviderInstallListener {
                override fun onProviderInstalled() {
                    Log.i("ApplicationInitial", "Security provider installed successfully")
                }

                override fun onProviderInstallFailed(errorCode: Int, recoveryIntent: android.content.Intent?) {
                    Log.e("ApplicationInitial", "Security provider installation failed with error code: $errorCode")
                }
            })
        } catch (e: Exception) {
            Log.e("ApplicationInitial", "Error initiating security provider installation", e)
        }
    }
}