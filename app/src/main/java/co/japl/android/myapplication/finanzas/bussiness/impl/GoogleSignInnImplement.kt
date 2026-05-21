package co.japl.android.myapplication.finanzas.bussiness.impl

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IGoogleSignInService
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes

class GoogleSignInnImplement constructor(private val context: Context): IGoogleSignInService {

    private var signInAccount:GoogleSignInAccount? = null
    private var googleSignInClient:GoogleSignInClient? = null
    private val GMAIL_SCOPE = "https://www.googleapis.com/auth/gmail.readonly"

    init {
        val googleSignInOptionsBuild = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestScopes(Scope(DriveScopes.DRIVE_FILE), Scope(DriveScopes.DRIVE_APPDATA), Scope(GMAIL_SCOPE))

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
            // Try Legacy Google Sign-In first as it is the one used by getConnection()
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                if (task.isSuccessful) {
                    signInAccount = task.result
                    message = "== Successful OK"
                    return message
                }
            } catch (e: Exception) {
                Log.d(javaClass.name, "Legacy Sign-In parsing failed: ${e.message}")
            }

            // Fallback to Identity API (Credential Manager) result parsing
            try {
                Identity.getAuthorizationClient(context).getAuthorizationResultFromIntent(data)
                    .toGoogleSignInAccount()?.let {
                    signInAccount = it
                    message = "== Successful OK Identity"
                    return message
                }
            } catch (e: Exception) {
                Log.d(javaClass.name, "Identity Sign-In parsing failed: ${e.message}")
            }
            
            signInAccount = null
            message = "== Sign-in failed: Could not parse account from intent"
        } else if(resultCode == Activity.RESULT_CANCELED){
            message = "== Canceled"
        } else if(resultCode == Activity.RESULT_FIRST_USER){
            message = "== First User"
        } else {
            message = "== Option not valid $resultCode"
        }
        return message
    }



    override fun check():Boolean {
       val account = GoogleSignIn.getLastSignedInAccount(context) ?: return false
       val hasDrive = account.grantedScopes.any { it.scopeUri == DriveScopes.DRIVE_FILE || it.scopeUri == DriveScopes.DRIVE_APPDATA }
       val hasGmail = account.grantedScopes.any { it.scopeUri == GMAIL_SCOPE }
       
       return if (hasDrive && hasGmail) {
           signInAccount = account
           account.grantedScopes.forEach{ Log.d(javaClass.name,"=== GrantedScope $it")}
           true
       } else {
           false
       }
    }

    override fun getAccount() = signInAccount

    fun setAccount(account:GoogleSignInAccount?) {
        signInAccount = account
    }
}