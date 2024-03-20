package co.japl.android.myapplication.finanzas.controller

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import co.com.japl.ui.interfaces.ISMSObservablePublicher
import co.com.japl.finances.iports.inbounds.common.ISMSRead
import co.com.japl.ui.Prefs
import co.japl.android.myapplication.finanzas.interfaces.ISMSBoadcastReceiver
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import javax.inject.Inject

/**
 * When this sms get the message it need setting with 3 variables in regex format expression
 * 0 all message
 * 1 value bought
 * 2 name bought
 * 3 date bought
 *
 * Example message
 * message:
 *  Bank paid $50.00 to storeHouse specialist. 13/02/2024 with CC account *1586 Any Question 645654654/654654
 * Regex:
 *  \W+ $(\d+\.\d+) to (\W+)\. (\d{2}\/\d{2}\/\d{4}) with CC account
 * Process Values get
 * 0 -> Bank paid $50.00 to storeHouse specialist. 13/02/2024 with CC account *1586 Any Question 645654654/654654
 * 1 -> 50.00
 * 2 -> storeHouse specialist
 * 3 -> 13/02/2024
 *
 * Note:
 * At the end of process this create a record on bought with credit card module and you can identify because it has a '*' at first of name
 * the month per default is 1 do you need to change into the UI if the month is more than one.
 * This has two setting receive in theory it are prepared and received sms from android the second it read the all sms
 *
 * You need to set sender and body regex uin UI to make it work
 * */
class SMS @Inject constructor(private val observable:ISMSObservablePublicher,private val context:Context):ISMSBoadcastReceiver ,
    ISMSRead, BroadcastReceiver(){

        override fun load(number: String,numDaysRead:Int):List<String>{
            val startDate = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).minusDays(numDaysRead.toLong())
            val list = arrayListOf<String>()
            val contentResolver = context.contentResolver
            val cursor = contentResolver.query(
                Telephony.Sms.CONTENT_URI,
                arrayOf(Telephony.Sms.ADDRESS,Telephony.Sms.BODY,Telephony.Sms.DATE_SENT),
                "${Telephony.Sms.ADDRESS} = ? AND ${Telephony.Sms.DATE_SENT} >= ? ",
                arrayOf(number,
                    startDate.toInstant(ZoneOffset.UTC).toEpochMilli().toString()),
                "${Telephony.Sms.DATE_SENT} DESC"
            )
            if(cursor != null && cursor.moveToFirst()){
                do{
                    val sender = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                    val message = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY))
                    val date = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE_SENT))
                    val sentDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.toLong()), ZoneId.systemDefault())
                    if(startDate.isBefore(sentDate) &&sender == number) {
                            list.add(message)
                    }
                }while (cursor.moveToNext())
            }
            cursor?.close()
            return list
        }

    private fun notify(sender:String?,number:String,message:String?){
        sender?.takeIf { sender == number}?.let{
            message?.takeIf { it.isNotEmpty() }?.let {
                observable.notify(number, it)
            }
        }
    }


override fun onReceive(context: Context?, intent: Intent?) {
            Telephony.Sms.Intents.getMessagesFromIntent(intent)?.takeIf { it.isNotEmpty() }?.forEach{
                val sender = it.originatingAddress
                val body = it.displayMessageBody
                for(number in observable.getNumbers()){
                        notify(sender, number, body)
                }
            }
        }

}