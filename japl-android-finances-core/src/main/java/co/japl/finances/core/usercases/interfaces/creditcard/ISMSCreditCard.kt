package co.japl.finances.core.usercases.interfaces.creditcard

import co.com.japl.finances.iports.dtos.SMSCreditCard
import co.com.japl.finances.iports.enums.KindInterestRateEnum

interface ISMSCreditCard {

    fun create(dto: SMSCreditCard):Int

    fun update(dto: SMSCreditCard):Boolean

    fun delete(codeSMSCreditCard: Int):Boolean

    fun getById(codeSMSCreditCard: Int): SMSCreditCard?

    fun validateMessagePattern(dto: SMSCreditCard):List<String>

    fun getByCreditCardAndKindInterest(codeCreditCard:Int,kind: KindInterestRateEnum):List<SMSCreditCard>
}