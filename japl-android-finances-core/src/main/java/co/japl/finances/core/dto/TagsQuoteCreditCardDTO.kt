package co.japl.finances.core.dto

import java.time.LocalDate

data class TagsQuoteCreditCardDTO(
    val id:Int,
    val create:LocalDate,
    val codQuote:Int,
    val codTag:Int,
    val active:Boolean
)

