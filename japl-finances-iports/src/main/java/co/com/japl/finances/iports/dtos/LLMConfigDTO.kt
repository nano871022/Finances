package co.com.japl.finances.iports.dtos

import co.com.japl.finances.iports.enums.LLMType

data class LLMConfigDTO(
    val type: LLMType,
    val apiKey: String,
    val isEnabled: Boolean = true
)
