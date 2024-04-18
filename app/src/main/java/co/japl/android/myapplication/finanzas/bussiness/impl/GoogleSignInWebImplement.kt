package co.japl.android.myapplication.finanzas.bussiness.impl

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IGoogleSignInService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.api.services.drive.DriveScopes

class GoogleSignInWebImplement constructor(private val context: Context): IGoogleSignInService {

    private var getCredentialRequest:GetCredentialRequest? = null
    private var account : GoogleIdTokenCredential? = null


    init {
        Log.d(javaClass.name,"=== GoogleSignWebImpl#init")
        val googleSignInOptionsBuild = GetGoogleIdOption
            .Builder()
            .setAutoSelectEnabled(true)
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(context.getString(R.string.default_web_client_id))
        Log.d(javaClass.name,"=== GoogleSignWebImpl#init step 2")
        val googleSignInOptions = googleSignInOptionsBuild.build()
        Log.d(javaClass.name,"=== GoogleSignWebImpl#init step 3")
        getCredentialRequest = GetCredentialRequest.Builder().addCredentialOption(googleSignInOptions).build()
        Log.d(javaClass.name,"=== GoogleSignWebImpl#init ${googleSignInOptions} $getCredentialRequest")
    }
    override fun getConnection(): Any? {
       return getCredentialRequest
    }

    fun getRequest():GetCredentialRequest? = getCredentialRequest

    override fun logoutAndOnComplete(task: () -> Unit) {

    }

    override fun login(requestCode:Int, resultCode:Int, data:Intent?):String {
       return "NOT ENABLED"
    }

    fun login(result:GetCredentialResponse){
        val credential = result.credential
        when(credential){
            is CustomCredential -> {
                if(credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
                    account = GoogleIdTokenCredential.createFrom(credential.data)
                }
            }
        }
    }



    override fun check():Boolean {
       return account != null
    }

    override fun getAccount() = account
}