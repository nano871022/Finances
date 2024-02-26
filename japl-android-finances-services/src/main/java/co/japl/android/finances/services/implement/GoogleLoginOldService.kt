package co.japl.android.finances.services.implement

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import co.japl.android.finances.services.R
import co.japl.android.finances.services.interfaces.IGoogleLoginService
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.SignInAccount
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient

class GoogleLoginOldService(private val activity: Activity, override val RC_SIGN_IN: Int): IGoogleLoginService,
    GoogleApiClient.OnConnectionFailedListener {

    private lateinit var signInAccount: GoogleSignInAccount
    private val googleSignInOptions = GoogleSignInOptions
        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()
    private val signInClient = GoogleApiClient.Builder(activity)
        .enableAutoManage(activity as FragmentActivity,this)
        .addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions)
        .build()

    override fun getIntent():Intent{
        return Auth.GoogleSignInApi.getSignInIntent(signInClient)
    }
    override fun check():Boolean{
        GoogleSignIn.getLastSignedInAccount(activity)?.let {
            signInAccount = it
            if(it.grantedScopes.isNotEmpty()){
                it.grantedScopes.forEach{ Log.d(this.javaClass.name,"Scope $it")}
            }
            return true
        }
        return false
    }

   override fun logout(){
       signInClient.let {
           if(it.isConnected) {
               Auth.GoogleSignInApi.signOut(signInClient)
           }
       }

    }

    override fun response(requestCode:Int, data:Intent){
        if(requestCode == RC_SIGN_IN){
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if(result?.isSuccess == true){
                result.signInAccount?.let {
                    signInAccount = it
                }
            }else{
                throw Exception("Sign in failed ${result?.status}")
            }
        }
    }

    override fun onConnectionFailed(resultConnection: ConnectionResult) {
        Log.d(javaClass.name,"Fail $resultConnection")
        //Toast.makeText(activity.baseContext,"Fail $resultConnection",Toast.LENGTH_LONG).show()
    }

    override fun getAccount():GoogleSignInAccount{
        return signInAccount
    }
}