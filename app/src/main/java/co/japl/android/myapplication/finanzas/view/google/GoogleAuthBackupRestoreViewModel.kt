package co.japl.android.myapplication.finanzas.view.google

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IGoogleLoginService
import com.google.android.gms.common.api.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class GoogleAuthBackupRestoreViewModel constructor(private val activity:Activity?,private val svc: IGoogleLoginService?): ViewModel() {
    var loginValue = mutableStateOf("Powered by Google LogIn")
    val result = mutableStateOf("***LOG INFO ********")
    val isLogged = mutableStateOf(false)
    val isProcessing = mutableStateOf(false)

    init{
        validation()
        svc?.subscribeMessage { svc.message().let {
            result.value = "${result.value?:""} \n ${it?:""}"
            if(it.lowercase(Locale.ROOT).contains("restore") ||it.lowercase(Locale.ROOT).contains("backup")){
                isProcessing.value = false
            }
        }}
    }

    fun responseConnection(activity:androidx.activity.result.ActivityResult){
        result.value = "${result.value} \n ${activity.resultCode} ${(activity.data?.extras?.keySet()?.map { it })?:"Any Information Found"}"
        if(activity.resultCode == Activity.RESULT_OK ) {
            activity.data?.let { data ->
                svc?.response(svc?.RC_SIGN_IN!!, activity.resultCode, data).also {
                    validation()
                }
            }
        }else{
            activity.data?.extras?.keySet()?.filter { it == "googleSignInStatus" }?.forEach{status->
                activity.data?.extras?.getBundle(status)?.keySet()?.forEach {bundleKey ->
                    activity.data?.extras?.getBundle(status)?.getString(bundleKey)?.let{
                        result.value = "${result.value} \n > $status => ${bundleKey}: $it"
                    }
                }
            }
            this.activity?.applicationContext?.let{
                Toast.makeText(it, R.string.error_login,Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validation(){
        svc?.let {
            if (svc.check()) {
                isLogged.value = true
                svc.getAccount()?.let {
                    it.email?.let { loginValue.value = it}
                }
                CoroutineScope(Dispatchers.IO).launch {
                    svc.infoBackup()?.let { result.value = "${result.value} \n $it" }
                }
            } else {
                isLogged.value = false
                loginValue.value = "Powered by Google LogIn"
            }
        }
    }

    fun login(): Intent? {
        return svc?.getIntent()
    }

    fun logout() {
        svc?.logout().also {
            isLogged.value = false
        }
    }

    fun backup() {
        isProcessing.value = true
        svc?.backup()
    }

    fun restore() {
        isProcessing.value = true
        svc?.restore()

    }
}