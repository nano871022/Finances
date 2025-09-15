package co.com.japl.module.creditcard.views.fakesSvc

import co.com.japl.finances.iports.dtos.SMSCreditCard
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.inbounds.creditcard.ISMSCreditCardPort
import java.time.LocalDateTime

class SMSCreditCardPortFake : ISMSCreditCardPort {
    override fun getSmsMessages(
        phoneNumber: String,
        pattern: String,
        numDaysRead: Int
    ): List<Triple<String, Double, LocalDateTime>> {
        return emptyList()
    }

    override fun getSmsMessages(
        pattern: String,
        sms: String
    ): Triple<String, Double, LocalDateTime>? {
        return null
    }

    override fun create(dto: SMSCreditCard): Int {
        return 1
    }

    override fun update(dto: SMSCreditCard): Boolean {
        return true
    }

    override fun delete(codeSMSCreditCard: Int): Boolean {
        return true
    }

    override fun getById(codeSMSCreditCard: Int): SMSCreditCard? {
        return null
    }

    override fun getByCodeCreditCard(codeCreditCard: Int): List<SMSCreditCard> {
        return emptyList()
    }

    override fun getByCreditCardAndKindInterest(
        codeCreditCard: Int,
        kind: KindInterestRateEnum
    ): List<SMSCreditCard> {
        return emptyList()
    }

    override fun enable(code: Int): Boolean {
        return true
    }

    override fun disable(code: Int): Boolean {
        return true
    }

    override fun validateMessagePattern(dto: SMSCreditCard): List<String> {
        return emptyList()
    }
}
