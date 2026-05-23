package co.japl.android.myapplication.finanzas.bussiness.impl

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContextCompat
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IGoogleSignInService
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes

class GoogleSignInnImplement(private val context: Context): IGoogleSignInService {

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
       val account = GoogleSignIn.getLastSignedInAccount(context)
       signInAccount = account
       return account != null
    }

    override fun getAccount() = signInAccount

    override fun isGoogleDriveGranted(): Boolean {
        return signInAccount?.grantedScopes?.any { 
            it.scopeUri == DriveScopes.DRIVE_FILE || it.scopeUri == DriveScopes.DRIVE_APPDATA 
        } ?: false
    }

    override fun isEmailAccessGranted(): Boolean {
        return signInAccount?.grantedScopes?.any { 
            it.scopeUri == GMAIL_SCOPE 
        } ?: false
    }

    override fun isSmsAccessGranted(): Boolean {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
    }

    override fun requestPermissions(activity: Activity) {
        val account = GoogleSignIn.getLastSignedInAccount(activity)
        val scopes = mutableListOf<Scope>()
        if (!isGoogleDriveGranted()) {
            scopes.add(Scope(DriveScopes.DRIVE_FILE))
            scopes.add(Scope(DriveScopes.DRIVE_APPDATA))
        }
        if (!isEmailAccessGranted()) {
            scopes.add(Scope(GMAIL_SCOPE))
        }
        
        if (scopes.isNotEmpty() && account != null) {
            GoogleSignIn.requestPermissions(activity, 101, account, *scopes.toTypedArray())
        } else {
            getConnection()?.let { intent ->
                activity.startActivityForResult(intent as Intent, 102)
            }
        }
    }

    override fun requestSmsPermission(activity: Activity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", activity.packageName, null)
        }
        activity.startActivity(intent)
    }

    fun setAccount(account:GoogleSignInAccount?) {
        signInAccount = account
    }
}