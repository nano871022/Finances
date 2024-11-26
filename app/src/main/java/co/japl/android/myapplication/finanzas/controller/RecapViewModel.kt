package co.japl.android.myapplication.finanzas.controller

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import co.com.japl.finances.iports.dtos.RecapDTO
import co.com.japl.finances.iports.inbounds.recap.IRecapPort
import co.com.japl.ui.Prefs
import co.japl.android.myapplication.R
import co.japl.finances.core.usercases.interfaces.creditcard.bought.lists.IBought
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import javax.inject.Inject

//@AndroidEntryPoint
class RecapViewModel @Inject constructor(private var recapSvc:IRecapPort?,private var boughtCreditCard:IBought?,private val prefs:Prefs, private val navController: NavController?) : ViewModel(){

    val cache = mutableStateOf(prefs.simulator)
    val msgInitial = mutableStateOf(prefs.msgInitial)
    private var _recap:RecapDTO? = null

    private var _currentProgress = mutableFloatStateOf(0f)
    private var _loading = mutableStateOf(true)

    val currentProgress get() = _currentProgress.value
    val loading get() = _loading.value
    val recap get() = _recap

    val totalInbound :Double get() = _recap?.totalInputs?:0.0
    val totalSaved : Double get() = _recap?.projectionSaved?:0.0
    val projectionsValue : Double get() = _recap?.projectionNext?:0.0
    val totalPayed : Double get() = _recap?.totalPaid?:0.0
    val totalCredits : Double get() =  _recap?.totalQuoteCredit?:0.0
    val totalCreditCard : Double get() =  _recap?.totalQuoteCreditCard?:0.0
    val warningValue : Double get() =  _recap?.warningValueCreditCard?:0.0


    fun setRecap(recap:RecapDTO){
        _recap = recap
    }

    fun main() = runBlocking {
        getRecap()
        _loading.value = false
        _currentProgress.value = 1f
    }

    suspend fun getRecap() = coroutineScope {
        launch {
            _currentProgress.value = 0.2f
            boughtCreditCard?.fixDataProcess()
            _currentProgress.value = 0.3f
            _recap = recapSvc?.getTotalValues(LocalDate.now(),cache.value)
            _currentProgress.value = 0.8f
        }
    }


}