package co.com.japl.module.creditcard.views.fakeSvc

import co.com.japl.finances.iports.dtos.SMSCreditCard
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.inbounds.creditcard.ISMSCreditCardPort
import java.time.LocalDateTime

class SMSCreditCardFake : ISMSCreditCardPort{
    override fun create(dto: SMSCreditCard): Int {
        TODO("Not yet implemented")
    }

    override fun update(dto: SMSCreditCard): Boolean {
        TODO("Not yet implemented")
    }

    override fun delete(codeSMSCreditCard: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun getById(codeSMSCreditCard: Int): SMSCreditCard? {
        TODO("Not yet implemented")
    }

    override fun validateMessagePattern(dto: SMSCreditCard): List<String> {
        TODO("Not yet implemented")
    }

    override fun getByCreditCardAndKindInterest(
        codeCreditCard: Int,
        kind: KindInterestRateEnum
    ): List<SMSCreditCard> {
        TODO("Not yet implemented")
    }

    override fun getAllByCodeCreditCard(codeCreditCard: Int): List<SMSCreditCard> {
        TODO("Not yet implemented")
    }

    override fun getSmsMessages(
        phoneNumber: String,
        pattern: String,
        numDaysRead: Int
    ): List<Triple<String, Double, LocalDateTime>> {
        TODO("Not yet implemented")
    }

    override fun getSmsMessages(
        pattern: String,
        sms: String
    ): Triple<String, Double, LocalDateTime>? {
        TODO("Not yet implemented")
    }

    override fun enable(codeSMSCreditCard: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun disable(codeSMSCreditCard: Int): Boolean {
        TODO("Not yet implemented")
    }
}