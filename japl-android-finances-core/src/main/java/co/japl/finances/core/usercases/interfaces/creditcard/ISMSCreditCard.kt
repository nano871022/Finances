package co.japl.finances.core.usercases.interfaces.creditcard

import co.com.japl.finances.iports.dtos.SMSCreditCard
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import java.time.LocalDateTime

interface ISMSCreditCard {

    fun create(dto: SMSCreditCard): Int

    fun update(dto: SMSCreditCard): Boolean

    fun delete(codeSMSCreditCard: Int): Boolean

    fun getById(codeSMSCreditCard: Int): SMSCreditCard?

    fun validateMessagePattern(dto: SMSCreditCard): List<String>

    fun getAllByCodeCreditCard(codeCreditCard: Int): List<SMSCreditCard>

    fun getByCreditCardAndKindInterest(
        codeCreditCard: Int,
        kind: KindInterestRateEnum
    ): List<SMSCreditCard>

    fun getSmsMessages(
        phoneNumber: String,
        pattern: String,numDaysRead:Int
    ): List<Triple<String, Double, LocalDateTime>>

    fun enable(codeSMSCreditCard: Int): Boolean

    fun disable(codeSMSCreditCard: Int): Boolean
}