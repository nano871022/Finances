package co.com.japl.finances.iports.dtos

data class CreditCardBoughtListDTO(
    val list:List<CreditCardBoughtItemDTO>,
    val recap:RecapCreditCardBoughtListDTO
)
