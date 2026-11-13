package co.japl.android.finances.services.implement

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import co.com.japl.finances.iports.outbounds.IGoogleSignInPort
import co.japl.android.finances.services.interfaces.IGoogleLoginService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.gmail.GmailScopes
import com.google.api.services.drive.DriveScopes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GoogleLoginService @Inject constructor(
    @ApplicationContext private val context: Context
) : IGoogleLoginService, IGoogleSignInPort {
    override val RC_SIGN_IN: Int = 101

    constructor(activity: Activity, RC_SIGN_IN: Int = 101) : this(activity.applicationContext)

    private var signInAccount: GoogleSignInAccount? = null
    private val googleSignInOptions = GoogleSignInOptions
        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestScopes(
            Scope(DriveScopes.DRIVE_APPDATA)
        )
        .build()
    val signInClient by lazy { GoogleSignIn.getClient(context, googleSignInOptions) }

    override fun logoutAndOnComplete(onComplete: () -> Unit) {
        signInClient.signOut().addOnCompleteListener { onComplete() }
    }

    override fun login(resultCode: Int, dataCode: Int, data: Intent): String {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        if (task.isSuccessful) {
            signInAccount = task.result
            return "Login Successful"
        } else {
            return "Login Failed: ${task.exception?.message}"
        }
    }

    override fun isGoogleDriveGranted(): Boolean = signInAccount?.grantedScopes?.any { it.scopeUri.contains("drive") } ?: false
    override fun isEmailAccessGranted(): Boolean = signInAccount?.grantedScopes?.any { it.scopeUri.contains("gmail") } ?: false
    override fun isSmsAccessGranted(): Boolean = true
    override fun getConnection(): Any? = getIntent()
    override fun requestPermissions(activity: Activity) {
        val scopes = arrayOf(
            Scope(DriveScopes.DRIVE_APPDATA),
            Scope(GmailScopes.GMAIL_READONLY)
        )
        signInAccount?.let {
            GoogleSignIn.requestPermissions(activity, RC_SIGN_IN, it, *scopes)
        }
    }
    override fun requestSmsPermission(activity: Activity) {}

    override fun getIntent(): Intent {
        return signInClient.signInIntent
    }

    override fun check(): Boolean {
        GoogleSignIn.getLastSignedInAccount(context)?.let {
            signInAccount = it
            return true
        }
        return false
    }

    override fun logout() {
        signInClient.signOut()
    }

    override fun response(requestCode: Int, data: Intent) {
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if (task.isSuccessful) {
                signInAccount = task.result
            } else {
                throw Exception("Sign in failed ${task.exception}")
            }
        }
    }

    override fun getAccount(): GoogleSignInAccount? {
        return signInAccount
    }
}
