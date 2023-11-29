package co.com.japl.finances.iports.dtos

import java.time.LocalDate

data class TagDTO(
    val id:Int,
    val create:LocalDate,
    val name:String,
    val active:Boolean
)

