package co.japl.android.finances.services.implement

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import co.japl.android.finances.services.R
import co.japl.android.finances.services.interfaces.IGoogleLoginService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class GoogleLoginService(private val activity:Activity, override val RC_SIGN_IN: Int) :IGoogleLoginService{
    private lateinit var signInAccount:GoogleSignInAccount
    private val googleSignInOptions = GoogleSignInOptions
        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()
    val signInClient = GoogleSignIn.getClient(activity,googleSignInOptions)
    override fun getIntent():Intent{
        return signInClient.signInIntent
    }
    override fun check():Boolean{
        GoogleSignIn.getLastSignedInAccount(activity)?.let {
            signInAccount = it
            if(it.grantedScopes.isNotEmpty()){
                it.grantedScopes.forEach{Log.d(this.javaClass.name,"$it")}
            }
            return true
        }
        return false
    }

    override fun logout(){
        signInClient.signOut()
    }

    override fun response(requestCode:Int,data:Intent){
        Log.d(this.javaClass.name,"Response Login: $data $requestCode ")
        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            Log.d(javaClass.name,"Response login ${task} ")
            if(task.isSuccessful){
                signInAccount = task.result
            }else{
                throw Exception("Sign in failed ${task.exception}")
            }
        }
    }



    override fun getAccount():GoogleSignInAccount{
        return signInAccount
    }
}