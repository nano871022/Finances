package co.com.japl.finances.iports.inbounds.common

import android.app.Activity

interface ISMSRead {
    fun load(number: String,numDaysRead:Int):List<String>
}