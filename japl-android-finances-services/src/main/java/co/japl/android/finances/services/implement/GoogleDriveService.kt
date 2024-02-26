package co.japl.android.finances.services.implement

import android.app.Activity
import android.content.Intent
import android.util.Log
import co.japl.android.finances.services.config.GoogleDriveConfig
import co.japl.android.finances.services.interfaces.ServiceListener
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.android.gms.drive.*

class GoogleDriveService(private val activity:Activity,private val config:GoogleDriveConfig) {
    var serviceListener : ServiceListener? = null
    private var driveClient: DriveClient? = null
    private var driveResourceClient: DriveResourceClient? = null
    private var signInAccount: GoogleSignInAccount? = null
    private val googleSignInClient: GoogleSignInClient by lazy{
        val builder = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        for ( scope in GoogleDriveService.SCOPES){
            builder.requestScopes(scope)
        }
        val signInOptions = builder.build()
        GoogleSignIn.getClient(activity,signInOptions)
    }
    private fun initializeDriveClient(signInAccount:GoogleSignInAccount){
        Log.d(this.javaClass.name,"InitializeDriveClient Start")
        driveClient  = Drive.getDriveClient(activity,signInAccount)
        driveResourceClient = Drive.getDriveResourceClient(activity, signInAccount)
        serviceListener?.loggedIn()
        Log.d(this.javaClass.name,"InitializeDriveClient Finish")
    }

    fun checkLoginStatus(){
        val requiredScopes = HashSet<Scope>(2)
        requiredScopes.add(Drive.SCOPE_FILE)
        requiredScopes.add(Drive.SCOPE_APPFOLDER)
        signInAccount = GoogleSignIn.getLastSignedInAccount(activity)
        val containsScope = signInAccount?.grantedScopes?.containsAll(requiredScopes)
        val account = signInAccount
        Log.d(this.javaClass.name,"checkLoginStatus $signInAccount $containsScope $account")
        if(account != null && containsScope == true){
            initializeDriveClient(account)
        }else{
            serviceListener?.cancelled("NOT LOGIN: Account: $account Scope: $containsScope")
        }
    }

    fun auth(){
        Log.d(this.javaClass.name,"<<<=== auth start ${googleSignInClient.signInIntent} $REQUEST_CODE_SIGN_IN")
        activity.startActivityForResult(googleSignInClient.signInIntent, GoogleDriveService.REQUEST_CODE_SIGN_IN)
        Log.d(this.javaClass.name,"<<<=== auth finish")
    }

    fun logout(){
        googleSignInClient.signOut()
        signInAccount = null
    }

    private fun handleSignIn(data: Intent){
        val getAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
        if(getAccountTask.isSuccessful){
            initializeDriveClient(getAccountTask.result)
        }else{
            Log.e(this.javaClass.name,"Sign in failed ${getAccountTask.exception}")
            serviceListener?.handleError(Exception("Sign in failed",getAccountTask.exception))
        }
    }


    private fun openItem(data:Intent){
        val driveId = data.getParcelableExtra<DriveId>(OpenFileActivityOptions.EXTRA_RESPONSE_DRIVE_ID)
        downloadFile(driveId)
    }

    private fun downloadFile(data:DriveId?){
        if(data == null){
            Log.e(this.javaClass.name,"DownloadFile is null")
            return
        }
        val drive = data.asDriveFile()
        val fileName = "test"

    }

    fun onActivityResults(requestCode: Int, resultCode: Int, data: Intent?){
        Log.d(this.javaClass.name, "onActivityResult $requestCode $data")
        when(requestCode){
            GoogleDriveService.REQUEST_CODE_SIGN_IN -> {
                Log.d(this.javaClass.name,"onActivityResults REQUEST CODE SIGN IN")
                if(data != null){
                    handleSignIn(data)
                }else{
                    serviceListener?.cancelled("REQUST CODE SIGHN IN Data: $data Code: $resultCode Request: $requestCode")
                }
            }
            GoogleDriveService.REQUEST_CODE_OPEN_ITEM->{
                Log.d(this.javaClass.name,"onActivityResults REQUEST CODE OPEN ITEM")
                if(data != null){
                    openItem(data)
                }else{
                    serviceListener?.cancelled("REQUEST CODE OPEN ITEM Data: $data Code: $resultCode Request: $requestCode")
                }
            }
            else -> {
                Log.d(this.javaClass.name,"Invalid option $requestCode")
            }
        }
    }


    companion object{
    val SCOPES = setOf<Scope>(Drive.SCOPE_FILE,Drive.SCOPE_APPFOLDER)
    val documentMimeTypes = arrayListOf("application/excel")
    const val REQUEST_CODE_OPEN_ITEM = 100
    const val REQUEST_CODE_SIGN_IN = 101
    const val TAG = "GoogleDriveService"
}





}

