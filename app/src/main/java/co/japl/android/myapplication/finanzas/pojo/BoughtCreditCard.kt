package co.japl.android.myapplication.finanzas.pojo

import co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.com.japl.finances.iports.dtos.DifferInstallmentDTO
import co.com.japl.finances.iports.dtos.RecapCreditCardBoughtListDTO
import java.time.LocalDateTime
import java.time.YearMonth

data class BoughtCreditCard(
    val recap:RecapCreditCardBoughtListDTO,
    val group:Map<YearMonth,List<CreditCardBoughtItemDTO>>,
    val creditCard:CreditCardDTO,
    val differQuotes:List<DifferInstallmentDTO>,
    val cutOff:LocalDateTime
)
