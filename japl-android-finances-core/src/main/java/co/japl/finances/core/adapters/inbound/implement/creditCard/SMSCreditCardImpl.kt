package co.japl.finances.core.adapters.inbound.implement.creditCard

import co.com.japl.finances.iports.dtos.SMSCreditCard
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.inbounds.creditcard.ISMSCreditCardPort
import co.japl.finances.core.usercases.interfaces.creditcard.ISMSCreditCard
import java.time.LocalDateTime
import javax.inject.Inject

class SMSCreditCardImpl @Inject constructor(private val svc:ISMSCreditCard) : ISMSCreditCardPort {
    override fun create(dto: SMSCreditCard): Int {
        require(dto.id == 0){"Id must be zero"}
        return svc.create(dto)
    }

    override fun update(dto: SMSCreditCard): Boolean {
        require(dto.id != 0){"Id must not be zero"}
        return svc.update(dto)
    }

    override fun delete(codeSMSCreditCard: Int): Boolean {
        require(codeSMSCreditCard != 0){"Id must not be zero"}
        return svc.delete(codeSMSCreditCard)
    }

    override fun getById(codeSMSCreditCard: Int): SMSCreditCard? {
        require(codeSMSCreditCard != 0){"Id must not be zero"}
        return svc.getById(codeSMSCreditCard)
    }

    override fun validateMessagePattern(dto: SMSCreditCard): List<String> {
        return svc.validateMessagePattern(dto)
    }

    override fun getByCreditCardAndKindInterest(
        codeCreditCard: Int,
        kind: KindInterestRateEnum
    ): List<SMSCreditCard> {
        require(codeCreditCard != 0){"Id must not be zero"}
        return svc.getByCreditCardAndKindInterest(codeCreditCard,kind)
    }

    override fun getAllByCodeCreditCard(codeCreditCard: Int): List<SMSCreditCard> {
        return svc.getAllByCodeCreditCard(codeCreditCard)
    }

    override fun getSmsMessages(
        phoneNumber: String,
        pattern: String,numDaysRead:Int
    ): List<Triple<String, Double, LocalDateTime>> {
        require(phoneNumber.isNotEmpty()){"Phone number must not be empty"}
        require(pattern.isNotEmpty()){"Pattern must not be empty"}
        return svc.getSmsMessages(phoneNumber,pattern,numDaysRead)
    }

    override fun enable(codeSMSCreditCard: Int): Boolean {
        return svc.enable(codeSMSCreditCard)
    }

    override fun disable(codeSMSCreditCard: Int): Boolean {
        return svc.disable(codeSMSCreditCard)
    }
}