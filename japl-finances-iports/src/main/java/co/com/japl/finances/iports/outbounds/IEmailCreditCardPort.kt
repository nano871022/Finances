package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.EmailCreditCardDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum

interface IEmailCreditCardPort {

    fun create(dto: EmailCreditCardDTO): Int

    fun update(dto: EmailCreditCardDTO): Boolean

    fun delete(codeEmailCreditCard: Int): Boolean

    fun getById(codeEmailCreditCard: Int): EmailCreditCardDTO?

    fun getByCodeCreditCard(codeCreditCard: Int): List<EmailCreditCardDTO>

    fun getByCreditCardAndKindInterest(codeCreditCard: Int, kind: KindInterestRateEnum): List<EmailCreditCardDTO>

}
