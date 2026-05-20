package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.EmailCreditCardDTO
import co.com.japl.finances.iports.dtos.EmailValidationDTO

interface IEmailCreditCardPattern {

    fun validateMessagePattern(dto: EmailCreditCardDTO): List<EmailValidationDTO>
}