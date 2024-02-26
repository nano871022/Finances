package co.com.japl.finances.iports.dtos

data class RecapCreditCardBoughtListDTO(
    var numBought:Int = 0,
    var numRecurrentBought:Int = 0,
    var numQuoteBought:Int = 0,
    var num1QuoteBought:Int = 0,
    var pendingToPay:Double = 0.0,
    var currentCapital:Double = 0.0,
    var currentInterest:Double = 0.0,
    var quoteCapital:Double = 0.0,
    var quoteInterest:Double = 0.0,
    var totalCapital:Double = 0.0,
    var totalInterest:Double = 0.0,
    var quoteValue:Double = 0.0,
    var numQuoteEnd:Int = 0,
    var totalQuoteEnd:Double = 0.0,
    var numNextQuoteEnd:Int = 0,
    var totalNextQuoteEnd:Double = 0.0
)
