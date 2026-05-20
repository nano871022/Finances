package co.com.japl.finances.iports.dtos

data class EmailValidationDTO(
    val name: String? = null,
    val value: String? = null,
    val date: String? = null,
    val matched: Boolean = false,
    val bodySnippet: String
)
