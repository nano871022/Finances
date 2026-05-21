package co.japl.finances.core.usercases.interfaces.paid

import co.com.japl.finances.iports.dtos.SMSPaidDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import java.time.LocalDateTime

interface ISMS {

    fun create(dto: SMSPaidDTO): Int

    fun update(dto: SMSPaidDTO): Boolean

    fun delete(codeSMSPaidDTO: Int): Boolean

    fun getById(codeSMSPaidDTO: Int): SMSPaidDTO?

    fun validateMessagePattern(dto: SMSPaidDTO): List<String>

    fun getAllByCodeAccount(codeAccount: Int): List<SMSPaidDTO>

    fun getSmsMessages(
        phoneNumber: String,
        pattern: String,numDaysRead:Int
    ): List<Triple<String, Double, LocalDateTime>>

    fun enable(codeSMSPaidDTO: Int): Boolean

    fun disable(codeSMSPaidDTO: Int): Boolean
}