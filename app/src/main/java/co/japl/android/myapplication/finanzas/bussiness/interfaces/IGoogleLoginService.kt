package co.japl.android.myapplication.finanzas.bussiness.interfaces

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface IGoogleLoginService {
    val RC_SIGN_IN:Int
    fun getIntent(): Intent
    fun check():Boolean
    fun logout()
    fun response(requestCode:Int, data:Intent)
    fun getAccount(): GoogleSignInAccount
}