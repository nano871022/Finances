package co.japl.android.finances.services.implement

import android.app.Activity
import android.content.Intent
import android.util.Log
import co.japl.android.finances.services.interfaces.IGoogleLoginService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.gmail.GmailScopes
import co.com.japl.finances.iports.inbounds.common.IGoogleSignInService

class GoogleLoginService(private val activity:Activity, override val RC_SIGN_IN: Int) :IGoogleLoginService, IGoogleSignInService {
    private var signInAccount:GoogleSignInAccount? = null
    private val googleSignInOptions = GoogleSignInOptions
        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestScopes(Scope(GmailScopes.GMAIL_READONLY))
        .build()
    val signInClient = GoogleSignIn.getClient(activity,googleSignInOptions)

    override fun logoutAndOnComplete(onComplete: () -> Unit) {
        signInClient.signOut().addOnCompleteListener { onComplete() }
    }

    override fun login(resultCode: Int, dataCode: Int, data: Intent): String {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        if(task.isSuccessful){
            signInAccount = task.result
            return "Login Successful"
        }else{
            return "Login Failed: ${task.exception?.message}"
        }
    }

    override fun isGoogleDriveGranted(): Boolean = signInAccount?.grantedScopes?.any { it.scopeUri.contains("drive") } ?: false
    override fun isEmailAccessGranted(): Boolean = signInAccount?.grantedScopes?.any { it.scopeUri.contains("gmail") } ?: false
    override fun isSmsAccessGranted(): Boolean = true
    override fun getConnection(): Any? = getIntent()
    override fun requestPermissions(activity: Activity) {}
    override fun requestSmsPermission(activity: Activity) {}

    override fun getIntent():Intent{
        return signInClient.signInIntent
    }
    override fun check():Boolean{
        GoogleSignIn.getLastSignedInAccount(activity)?.let {
            signInAccount = it
            return true
        }
        return false
    }

    override fun logout(){
        signInClient.signOut()
    }

    override fun response(requestCode:Int,data:Intent){
        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if(task.isSuccessful){
                signInAccount = task.result
            }else{
                throw Exception("Sign in failed ${task.exception}")
            }
        }
    }

    override fun getAccount():GoogleSignInAccount?{
        return signInAccount
    }
}
