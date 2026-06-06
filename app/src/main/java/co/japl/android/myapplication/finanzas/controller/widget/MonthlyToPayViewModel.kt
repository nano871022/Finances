package co.japl.android.myapplication.finanzas.controller.widget

import android.content.Context
import co.com.japl.finances.iports.dtos.RecapDTO
import co.com.japl.ui.Prefs
import co.com.japl.ui.utils.NumbersUtil
import co.com.japl.ui.utils.WindowWidthSize
import co.japl.android.myapplication.finanzas.modules.EntryPoint
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

class MonthlyToPayViewModel (val context: Context, private val prefs:Prefs){
    val entryPoint = EntryPointAccessors.fromApplication(context, EntryPoint::class.java)
    val recapSvc = entryPoint.getRecapSvc()
    var dto: Result<RecapDTO>? = null

    fun value(value:Double,widthSz: WindowWidthSize):String{
        if(widthSz == WindowWidthSize.COMPACT){
            return "${NumbersUtil.COPtoString(value / 1000)}K"
        }
        return NumbersUtil.COPtoString(value)
    }

    suspend fun load(){
        withContext(Dispatchers.IO) {
            runCatching {
                recapSvc.getTotalValues(LocalDate.now(), prefs.simulator)
            }.let{
                dto = it
            }
        }
    }
}