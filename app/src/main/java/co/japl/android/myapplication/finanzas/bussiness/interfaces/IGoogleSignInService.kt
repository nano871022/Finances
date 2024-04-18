package co.japl.android.myapplication.finanzas.bussiness.interfaces

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface IGoogleSignInService {

    fun getConnection():Any?
    fun logoutAndOnComplete(task: () -> Unit)

    fun login(requestCode:Int, resultCode:Int, data: Intent?):String?

    fun check():Boolean

    fun getAccount(): Any?

}