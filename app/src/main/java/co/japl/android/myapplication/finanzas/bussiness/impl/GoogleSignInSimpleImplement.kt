package co.japl.android.myapplication.finanzas.bussiness.impl

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IGoogleSignInService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes

class GoogleSignInSimpleImplement constructor(private val context: Context): IGoogleSignInService {

    private var signInAccount:GoogleSignInAccount? = null
    private var googleSignInClient:GoogleSignInClient? = null

    init {
        val googleSignInOptionsBuild = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)

        val googleSignInOptions = googleSignInOptionsBuild.build()
        googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions)
    }
    override fun getConnection(): Any? {
       return googleSignInClient?.signInIntent
    }

    override fun logoutAndOnComplete(task: () -> Unit) {
        googleSignInClient?.signOut()?.addOnCompleteListener {
            task.invoke()
        }
    }

    override fun login(requestCode:Int, resultCode:Int, data:Intent?):String {
        var message = ""
        if(resultCode == Activity.RESULT_OK && data != null){
            GoogleSignIn.getSignedInAccountFromIntent(data).let{
                with(it){
                    if(isSuccessful) {
                        signInAccount = this.result
                        message = "== Successful OK"
                    }else if(isCanceled){
                        signInAccount = null
                        message =  "== Canceled $exception"
                    }else if(isComplete){
                        signInAccount = this.result
                       message = "== Complete OK"
                    }else{
                        throw Exception("Sign in failed Option does not exists")
                    }
                }
            }
        }else if(resultCode == Activity.RESULT_CANCELED){
            message = "== Canceled"
        }else if(resultCode == Activity.RESULT_FIRST_USER){
            message = "== First User"
        }else{
            message = "== Option not valid $resultCode"
        }
        return message
    }



    override fun check():Boolean {
       return GoogleSignIn.getLastSignedInAccount(context)
           ?.takeIf{it.grantedScopes.isNotEmpty()}
           ?.let{
               signInAccount = it
               it.grantedScopes.forEach{ Log.d(javaClass.name,"=== GrantedScope $it")}
               true
           }?:false
    }

    override fun getAccount() = signInAccount
}