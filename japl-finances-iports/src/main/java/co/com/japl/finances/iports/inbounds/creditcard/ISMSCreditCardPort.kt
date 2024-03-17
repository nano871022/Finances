package co.com.japl.finances.iports.inbounds.creditcard

import co.com.japl.finances.iports.dtos.SMSCreditCard
import co.com.japl.finances.iports.enums.KindInterestRateEnum

interface ISMSCreditCardPort{

    fun create(dto:SMSCreditCard):Int

    fun update(dto:SMSCreditCard):Boolean

    fun delete(codeSMSCreditCard: Int):Boolean

    fun getById(codeSMSCreditCard: Int):SMSCreditCard?

    fun validateMessagePattern(dto:SMSCreditCard):List<String>

    fun getByCreditCardAndKindInterest(codeCreditCard:Int,kind: KindInterestRateEnum):List<SMSCreditCard>

}