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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.runBlocking
import java.util.Locale

class GoogleAuthBackupRestoreViewModel constructor(private val activity:Activity?,private val loginSvc: IGoogleSignInService?,private val loginSimpleSvc: IGoogleSignInService?,private val loginWebSvc: IGoogleSignInService?,private val driveSvc: IGoogleDriveService?): ViewModel() {
    var loginValue = mutableStateOf("Powered by Google LogIn")
    val result = mutableStateOf("***LOG INFO ********")
    val isLogged = mutableStateOf(false)
    val isProcessing = mutableStateOf(false)
    val statsLocalProgess = mutableStateOf(true)
    val statsLocal = mutableStateListOf<Pair<String,Long>>()
    val tabIndex = mutableIntStateOf(0)

    init{
        CoroutineScope(Dispatchers.IO).launch {
            validation()
        }
    }

    fun logout() {
        viewModelScope.launch {
            isProcessing.value = true
            loginSvc?.logoutAndOnComplete {
                isLogged.value = false
                result.value = "${result.value} \n == LogOut Successful"
                isProcessing.value = false
            }
        }
    }

     fun responseConnection(activity:androidx.activity.result.ActivityResult){
         val actvty = this.activity
         CoroutineScope(Dispatchers.IO).launch {
             result.value = "${result.value} \n ${activity.resultCode} ${
                 (activity.data?.extras?.keySet()?.map { it }) ?: "Any Information Found"
             }"
             if (activity.resultCode == Activity.RESULT_OK) {
                 activity.data?.let { data ->
                     loginSvc?.login(activity.resultCode, activity.resultCode, data).let {
                         result.value = "${result.value} \n $it"
                         validation()
                     }
                 }
             } else {
                 activity.data?.extras?.keySet()?.filter { it.isNotBlank() }?.forEach { status ->
                     activity.data?.extras?.getBundle(status)?.keySet()?.forEach { bundleKey ->
                         activity.data?.extras?.getBundle(status)?.getString(bundleKey)?.let {
                             result.value = "${result.value} \n > $status => ${bundleKey}: $it"
                         }
                     }
                 }
                 result.value = "=== ${result.value} \n ${actvty?.getString(R.string.error_login)}"
             }
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

   suspend fun backup() {
       isProcessing.value = true
            (loginSvc?.getAccount() as GoogleSignInAccount)?.let {
                driveSvc?.backup(it)?.let {
                    result.value = "${result.value} \n $it"
                    isProcessing.value = false
                }
            }
    }

    fun onload(){
        CoroutineScope(Dispatchers.IO).launch {
            statsLocal()
        }
    }

    fun restore() {
        isProcessing.value = true
        CoroutineScope(Dispatchers.IO).launch {
            isProcessing.value.takeIf { it }?.let {
                (loginSvc?.getAccount() as GoogleSignInAccount)?.let {
                    driveSvc?.restore(it)?.let {
                        result.value = "${result.value} \n $it"
                        isProcessing.value = false
                    }
                }
            }
        }
    }

    private suspend fun statsLocal() {
        statsLocalProgess.value.takeIf { it }?.let {
           driveSvc?.stats()?.let {
               statsLocal.clear()
               statsLocal.addAll(it)
               statsLocalProgess.value = false
           }
       }
    }
}