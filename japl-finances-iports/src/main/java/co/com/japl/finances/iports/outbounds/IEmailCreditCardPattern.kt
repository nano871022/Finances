package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.EmailCreditCardDTO

interface IEmailCreditCardPattern {

    fun validateMessagePattern(dto: EmailCreditCardDTO): List<String>
}