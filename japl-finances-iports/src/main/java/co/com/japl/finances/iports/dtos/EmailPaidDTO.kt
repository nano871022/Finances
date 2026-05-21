package co.com.japl.finances.iports.dtos

data class EmailPaidDTO(
    val id: Int,
    val sender: String,
    val subjectPattern: String,
    val bodyPattern: String,
    val codeAccount: Int,
    val nameAccount: String,
    val active: Boolean
)
