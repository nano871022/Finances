package co.japl.android.myapplication.finanzas.view.google

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.credentials.GetCredentialException
import android.os.Build
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.ViewModel
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.impl.GoogleDriveImpl
import co.japl.android.myapplication.finanzas.bussiness.impl.GoogleSignInWebImplement
import co.japl.android.myapplication.finanzas.bussiness.impl.GoogleSignInnImplement
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IGoogleDriveService
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IGoogleLoginService
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IGoogleSignInService
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.GoogleApi
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import com.google.android.gms.common.api.Status
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.services.drive.DriveScopes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class GoogleAuthBackupRestoreViewModel constructor(private val activity:Activity?,private val loginSvc: IGoogleSignInService?,private val loginSimpleSvc: IGoogleSignInService?,private val loginWebSvc: IGoogleSignInService?,private val driveSvc: IGoogleDriveService?): ViewModel() {
    var loginValue = mutableStateOf("Powered by Google LogIn")
    val result = mutableStateOf("***LOG INFO ********")
    val isLogged = mutableStateOf(false)
    val isProcessing = mutableStateOf(false)

    init{
        CoroutineScope(Dispatchers.IO).launch {
            validation()
        }
    }

    suspend fun responseConnection(activity:androidx.activity.result.ActivityResult){
        result.value = "${result.value} \n ${activity.resultCode} ${(activity.data?.extras?.keySet()?.map { it })?:"Any Information Found"}"
        if(activity.resultCode == Activity.RESULT_OK ) {
            activity.data?.let { data ->
                loginSvc?.login(activity.resultCode, activity.resultCode, data).let {
                    result.value = "${result.value} \n $it"
                    validation()
                }
            }
        }else{
            activity.data?.extras?.keySet()?.forEach{status->
                activity.data?.extras?.getBundle(status)?.keySet()?.forEach {bundleKey ->
                    activity.data?.extras?.getBundle(status)?.getString(bundleKey)?.let{
                        result.value = "${result.value} \n > $status => ${bundleKey}: $it"
                    }
                }
            }
            result.value = "=== ${result.value} \n ${this.activity?.getString(R.string.error_login)}"
        }
    }

    private suspend fun validation(){
        try {
            isProcessing.value = true
            loginSvc?.let {
                if (loginSvc.check()) {
                    isLogged.value = true
                    (loginSvc.getAccount() as GoogleSignInAccount).let {
                        it.email?.let { loginValue.value = it }
                        driveSvc?.infoBackup(it)?.let {
                            result.value = "${result.value} \n $it"
                            isProcessing.value = false
                        }
                    }
                } else {
                    isLogged.value = false
                    loginValue.value = "Powered by Google LogIn"
                    isProcessing.value = false
                }
            }
        }catch(e:GoogleJsonResponseException){
            e.details.forEach{
                result.value = "${result.value} \n ${it.key}: ${it.value}"
            }
        }
    }

    fun login(): Intent? {
        return loginSvc?.getConnection() as Intent
    }

    fun loginSimple(): Intent? {
        return loginSimpleSvc?.getConnection() as Intent
    }

    suspend fun loginWeb2(context: Context, launch:(Intent)->Unit){
        try {
            val requestScopes = mutableListOf(Scope(DriveScopes.DRIVE_FILE))
            val authorizationRequest =
                AuthorizationRequest.builder().setRequestedScopes(requestScopes).build()
            Identity.getAuthorizationClient(context).authorize(authorizationRequest)
                .addOnSuccessListener {
                    try {
                        if (it.hasResolution()) {
                            val pendingIntent = it.pendingIntent
                            val intentSenderRequest =
                                IntentSenderRequest.Builder(pendingIntent?.intentSender!!).build()
                            val intent = ActivityResultContracts.StartIntentSenderForResult()
                                .createIntent(context, intentSenderRequest)
                            launch.invoke(intent)
                        } else {
                            (loginSvc as GoogleSignInnImplement).setAccount(it.toGoogleSignInAccount())
                            result.value = "${result.value} \n Login ${it}"
                            if (it.grantedScopes.isNotEmpty()) {
                                GoogleSignIn.getLastSignedInAccount(context)?.let {
                                    result.value = "${result.value} \n ${it.id}"
                                }
                                result.value =
                                    "${result.value} \n ${it.toGoogleSignInAccount()?.displayName} ${it.toGoogleSignInAccount()?.email} ${it.toGoogleSignInAccount()?.id} ${it.toGoogleSignInAccount()?.account?.name}"
                                (driveSvc as GoogleDriveImpl).getDrive(it)?.let {
                                    result.value = "${result.value} \n Drive connection get"
                                    it.Files().list().setSpaces(GoogleDriveImpl.PARAMETER_FOLDER)
                                        .setFields("nextPageToken, files(id, name, version, modifiedTime)")
                                        .execute().files.takeIf { it.isNotEmpty() }?.forEach {
                                            result.value =
                                                "${result.value} \n ${it.id} ${it.name} ${it.version} ${it.modifiedTime}"
                                        }
                                }
                            }
                        }
                    }catch (e:Exception){
                        result.value = "${result.value} \n Exception ${e.message}"
                    }
                }.addOnFailureListener {
                    result.value = "${result.value} \n Exception: ${it.message}"
                }
        }catch(e:Exception){
            result.value = "${result.value} \n Exception ${e.message}"
        }
    }

   @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
   suspend fun loginWeb(context:Context) {
       try {
           val credentialManager =
               CredentialManager.create(context = context)
           val request = (loginWebSvc as GoogleSignInWebImplement).getRequest() as GetCredentialRequest

           credentialManager.getCredential(context = context, request = request)
                       .let {
                           (loginWebSvc as GoogleSignInWebImplement).login(it)
                       }
           loginWebSvc.getAccount()?.let{
               isLogged.value = true
               it.displayName?.let { loginValue.value = it }

           }

       }catch (e:GetCredentialException){
           result.value = "${result.value} \n ${e.message}"
       }catch (e:GoogleIdTokenParsingException){
           result.value = "${result.value} \n ${e.message}"
       }catch(e:NoCredentialException){
           result.value = "${result.value} \n ${e.message}"
       }
    }

    suspend fun logout() {
        isProcessing.value = true
        loginSvc?.logoutAndOnComplete{
            isLogged.value = false
            result.value = "${result.value} \n == LogOut Successful"
            isProcessing.value = false
        }
    }

   suspend fun backup() {
        isProcessing.value = true
       (loginSvc?.getAccount() as GoogleSignInAccount)?.let{
            driveSvc?.backup(it)?.let{
                result.value = "${result.value} \n $it"
                isProcessing.value = false
            }
        }
    }

    suspend fun restore() {
        isProcessing.value = true
        (loginSvc?.getAccount() as GoogleSignInAccount)?.let{
            driveSvc?.restore(it)?.let{
                result.value = "${result.value} \n $it"
                isProcessing.value = false
            }
        }

    }
}