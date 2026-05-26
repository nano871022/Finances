package co.com.japl.finances.iports.inbounds.common

import android.app.Activity
import java.time.LocalDateTime

interface ISMSRead {
    fun load(number: String,numDaysRead:Int):List<Pair<String, LocalDateTime>>
}