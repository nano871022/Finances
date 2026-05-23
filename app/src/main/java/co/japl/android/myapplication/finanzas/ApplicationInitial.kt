package co.japl.android.myapplication.finanzas

import android.app.Application
import android.util.Log
import co.com.japl.ui.Prefs
import co.japl.android.myapplication.finanzas.bussiness.DB.connections.ConnectDB
import com.google.android.gms.security.ProviderInstaller
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ApplicationInitial:Application() {
    companion object {
        lateinit var prefs : Prefs
    }
    @Inject lateinit var connectDB: ConnectDB

    override fun onCreate() {
        super.onCreate()
        prefs = Prefs(applicationContext)
        preWarningDB()
        providerInstaller()
    }

    private fun preWarningDB(){
        Thread{
            try {
                connectDB.writableDatabase
                Log.i(this.javaClass.name,"=== DB OK")
            }catch (e:Exception){
                Log.e(this.javaClass.name," ERROR $e",e)
            }
        }.start()
    }

    private fun providerInstaller(){
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