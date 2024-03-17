package co.japl.finances.core.usercases.implement.creditcard

import co.com.japl.finances.iports.dtos.SMSCreditCard
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.inbounds.common.ISMSRead
import co.com.japl.finances.iports.outbounds.ISMSCreditCardPort
import co.japl.finances.core.usercases.interfaces.creditcard.ISMSCreditCard
import javax.inject.Inject

class SMSCreditCardImpl @Inject constructor(private val svc:ISMSCreditCardPort, private val smsSvc:ISMSRead): ISMSCreditCard {
    override fun create(dto: SMSCreditCard): Int {
        return svc.create(dto)
    }

    override fun update(dto: SMSCreditCard): Boolean {
        return svc.update(dto)
    }

    override fun delete(codeSMSCreditCard: Int): Boolean {
        return svc.delete(codeSMSCreditCard)
    }

    override fun getById(codeSMSCreditCard: Int): SMSCreditCard? {
        return svc.getById(codeSMSCreditCard)
    }

    override fun validateMessagePattern(dto: SMSCreditCard): List<String> {
        return emptyList()
    }

    override fun getByCreditCardAndKindInterest(
        codeCreditCard: Int,
        kind: KindInterestRateEnum
    ): List<SMSCreditCard> {
        return svc.getByCreditCardAndKindInterest(codeCreditCard,kind)
    }
}