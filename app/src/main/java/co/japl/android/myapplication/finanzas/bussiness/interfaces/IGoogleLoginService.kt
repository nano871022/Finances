package co.japl.android.myapplication.finanzas.bussiness.interfaces

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface IGoogleLoginService {
    val RC_SIGN_IN:Int
    fun getIntent(): Intent
    fun check():Boolean
    fun logout()

    suspend fun login()
    fun response(requestCode:Int, resultCode:Int,data:Intent)
    fun getAccount(): GoogleSignInAccount

    fun restore()

    fun backup()

    fun infoBackup():String

    fun message():String

    fun subscribeMessage(subscriber:()->Unit):Boolean
}