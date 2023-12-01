package co.japl.finances.core.usercases.calculations

import co.com.japl.finances.iports.dtos.CreditCardBoughtItemDTO
import co.com.japl.finances.iports.dtos.RecapCreditCardBoughtListDTO
import javax.inject.Inject

class RecapCalculation @Inject constructor() {

    internal fun getRecap(recap:RecapCreditCardBoughtListDTO, dto: CreditCardBoughtItemDTO){
        recap.numBought += 1
        recap.numRecurrentBought += if(dto.recurrent) 1 else 0
        recap.numQuoteBought +=  if(dto.recurrent.not() && dto.month > 1 ) 1 else 0
        recap.num1QuoteBought += if(dto.recurrent.not() && dto.month == 1) 1 else 0

        recap.currentCapital += if(dto.month == 1)dto.capitalValue else 0.0
        recap.currentInterest += if(dto.month == 1)dto.interestValue else 0.0
        recap.quoteCapital += if(dto.month > 1 && dto.monthPaid > 0) dto.quoteValue else 0.0
        recap.quoteInterest += if(dto.month > 1 && dto.monthPaid > 0) dto.interestValue else 0.0
        recap.totalCapital += dto.capitalValue
        recap.totalInterest += dto.interestValue
        recap.quoteValue += dto.quoteValue
        recap.pendingToPay += dto.pendingToPay

        recap.numQuoteEnd += if(dto.month == (dto.monthPaid + 1).toInt()) 1 else 0
        recap.totalQuoteEnd += if(dto.month == (dto.monthPaid + 1).toInt()) dto.quoteValue else 0.0
        recap.numNextQuoteEnd += if(dto.month == (dto.monthPaid + 2).toInt()) 1 else 0
        recap.totalNextQuoteEnd += if(dto.month == (dto.monthPaid + 2).toInt()) dto.quoteValue else 0.0
    }
}