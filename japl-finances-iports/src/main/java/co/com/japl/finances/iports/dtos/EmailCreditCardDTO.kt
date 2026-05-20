package co.com.japl.finances.iports.dtos

import co.com.japl.finances.iports.enums.KindInterestRateEnum

data class EmailCreditCardDTO(
    val id: Int,
    val sender: String,
    val subjectPattern: String,
    val bodyPattern: String,
    val codeCreditCard: Int,
    val nameCreditCard: String,
    val kindInterestRateEnum: KindInterestRateEnum,
    val active: Boolean
)
