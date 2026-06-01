package co.japl.android.myapplication.finanzas.controller.google

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.japl.android.myapplication.R
import co.com.japl.finances.iports.dtos.StorageInfo
import co.com.japl.finances.iports.inbounds.common.IGoogleDriveService
import co.com.japl.finances.iports.inbounds.common.IGoogleSignInService
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class GoogleAuthBackupRestoreViewModel(private val activity:Activity?, private val loginSvc: IGoogleSignInService?, private val driveSvc: IGoogleDriveService?): ViewModel() {
    var loginValue = mutableStateOf("Powered by Google LogIn")
    var nameValue = mutableStateOf("name")
    var photoUrlValue = mutableStateOf<String?>(null)
    val result = mutableStateOf("***LOG INFO ********")
    val isLogged = mutableStateOf(false)
    val isProcessing = mutableStateOf(false)
    val statsLocalProgess = mutableStateOf(true)
    val statsLocal = mutableStateListOf<Pair<String,Long>>()
    val tabIndex = mutableIntStateOf(0)
    val spaceUsed = mutableStateOf(0.0)
    val spaceMax = mutableStateOf(0.0)
    val lastBackup = mutableStateOf(LocalDateTime.now())
    val spaceDBKb = mutableStateOf(0.0)

    val isGoogleDriveGranted = mutableStateOf(false)
    val isEmailAccessGranted = mutableStateOf(false)
    val isSmsAccessGranted = mutableStateOf(false)
    val isAllPermissionsGranted = derivedStateOf { isGoogleDriveGranted.value && isEmailAccessGranted.value && isSmsAccessGranted.value }

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
                isGoogleDriveGranted.value = false
                isEmailAccessGranted.value = false
                isSmsAccessGranted.value = false
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
                 isProcessing.value = false
             }
         }
    }

    private suspend fun validation(){
        try {
            isProcessing.value = true
            loginSvc?.let { svc ->
                if (svc.check()) {
                    isLogged.value = true
                    (svc.getAccount() as? GoogleSignInAccount)?.let { account ->
                        account.email?.let { loginValue.value = it }
                        account.displayName?.let { nameValue.value = it }
                        account.photoUrl?.let { photoUrlValue.value = it.toString() }
                        
                        isGoogleDriveGranted.value = svc.isGoogleDriveGranted()
                        isEmailAccessGranted.value = svc.isEmailAccessGranted()

                        if (account.email?.isNotBlank() == true) {
                            driveSvc?.infoBackup(account)?.let { info ->
                                result.value = "${result.value} \n $info"
                                updateStorageInfo(account)
                            }
                        }
                    }
                } else {
                    isLogged.value = false
                    loginValue.value = "Powered by Google LogIn"
                }
            }
            checkSmsPermission()
        }catch(e:GoogleJsonResponseException){
            e.details.forEach{
                result.value = "${result.value} \n ${it.key}: ${it.value}"
            }
        } catch(e:Exception){
            result.value = "${result.value} \n Error: ${e.message}"
        } finally {
            isProcessing.value = false
        }
    }

    fun checkSmsPermission() {
        isSmsAccessGranted.value = loginSvc?.isSmsAccessGranted() ?: false
    }

    private suspend fun updateStorageInfo(account: GoogleSignInAccount) {
        if (account.email?.isNotBlank() == true) {
            driveSvc?.getStorageInfo(account)?.let { info ->
                spaceUsed.value = info.spaceUsed.toDouble()
                spaceMax.value = info.spaceMax.toDouble()
                info.lastBackup?.let { lastBackup.value = it }
                spaceDBKb.value = info.spaceDBKb.toDouble()
            }
        }
    }

    fun login(): Intent? {
        return loginSvc?.getConnection() as? Intent
    }

    fun grantGooglePermissions() {
        activity?.let {
            loginSvc?.requestPermissions(it)
        }
    }

    fun grantSmsPermission() {
        activity?.let {
            loginSvc?.requestSmsPermission(it)
        }
    }

   suspend fun backup() {
       isProcessing.value = true
       (loginSvc?.getAccount() as? GoogleSignInAccount)?.let { account ->
           if (account.email?.isNotBlank() == true) {
               driveSvc?.backup(account)?.let {
                   spaceDBKb.value = it.toDouble()
                   result.value = "${result.value} \n $it"
                   updateStorageInfo(account)

                   isProcessing.value = false
               }
           } else {
               isProcessing.value = false
           }
       } ?: run {
           isProcessing.value = false
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
                (loginSvc?.getAccount() as? GoogleSignInAccount)?.let { account ->
                    if (account.email?.isNotBlank() == true) {
                        driveSvc?.restore(account)?.let {
                            result.value = "${result.value} \n $it"
                            updateStorageInfo(account)
                            isProcessing.value = false
                        }
                    } else {
                        isProcessing.value = false
                    }
                } ?: run {
                    isProcessing.value = false
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
