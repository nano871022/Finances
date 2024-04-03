package co.com.japl.finances.iports.inbounds.paid

import co.com.japl.finances.iports.dtos.SMSPaidDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import java.time.LocalDateTime

interface ISMSPaidPort{

    fun create(dto:SMSPaidDTO):Int

    fun update(dto:SMSPaidDTO):Boolean

    fun delete(codeSMSPaid: Int):Boolean

    fun getById(codeSMSPaid: Int):SMSPaidDTO?

    fun validateMessagePattern(dto:SMSPaidDTO):List<String>

    fun getAllByCodeAccount(codeAccount: Int):List<SMSPaidDTO>

    fun getSmsMessages(phoneNumber:String,pattern:String,numDaysRead:Int):List<Triple<String,Double, LocalDateTime>>

    fun enable(codeSMSPaid: Int):Boolean

    fun disable(codeSMSPaid: Int):Boolean

}