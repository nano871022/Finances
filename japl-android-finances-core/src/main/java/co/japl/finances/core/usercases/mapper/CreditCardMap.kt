package co.japl.finances.core.usercases.mapper

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import co.com.japl.finances.iports.dtos.CreditCardDTO
import co.japl.finances.core.model.CreditCard
import co.japl.finances.core.utils.Config
import java.time.LocalDateTime
import java.util.*

object CreditCardMap {

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapper(creditCard:CreditCardDTO): CreditCard {
        return CreditCard(
            codeCreditCard = creditCard.id,
            nameCreditCard = creditCard.name,
            cutoffDay = creditCard.cutOffDay,
            cutOff = Config.nextCutOff( creditCard.cutOffDay.toInt() ),
            warningValue = creditCard.warningValue.toDouble(),
            maxQuotes = creditCard.maxQuotes,
            capital = 0.0,
            capitalQuote = 0.0,
            capitalQuotes = 0.0,
            interest = 0.0,
            interestQuote = 0.0,
            interestQuotes = 0.0,
            quotes = 0,
            oneQuote = 0,
            quotesPending = 0,
            lastTax = 0.0,
            cutOffLast = LocalDateTime.now(),
            interest1NotQuote = creditCard.interest1NotQuote,
            interest1Quote = creditCard.interest1Quote
        )
    }

}