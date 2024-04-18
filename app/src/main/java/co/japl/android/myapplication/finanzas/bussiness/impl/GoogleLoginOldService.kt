package co.japl.android.myapplication.finanzas.bussiness.impl

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IGoogleLoginService
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

    override suspend fun login() {
        TODO("Not yet implemented")
    }

    override fun response(requestCode:Int,resultCode:Int, data:Intent){
        if(requestCode == RC_SIGN_IN){
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if(result?.isSuccess == true){
                result.signInAccount?.let {
                    signInAccount = it
                }
            }else{
                Toast.makeText(activity.applicationContext,activity.resources.getString(R.string.it_is_not_possible_log_in),Toast.LENGTH_LONG).show()
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

    override fun restore() {
        TODO("Not yet implemented")
    }

    override fun backup() {
        TODO("Not yet implemented")
    }

    override fun infoBackup(): String {
        TODO("Not yet implemented")
    }

    override fun message(): String {
        TODO("Not yet implemented")
    }

    override fun subscribeMessage(subscriber: () -> Unit): Boolean {
        TODO("Not yet implemented")
    }


}