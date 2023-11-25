package co.japl.finances.core.dto

import java.time.LocalDate

data class TagDTO(
    val id:Int,
    val create:LocalDate,
    val name:String,
    val active:Boolean
)

