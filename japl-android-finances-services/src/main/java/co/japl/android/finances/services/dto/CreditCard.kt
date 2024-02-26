package co.japl.android.finances.services.dto

import android.os.Build
import androidx.annotation.RequiresApi
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
@RequiresApi(Build.VERSION_CODES.N)
class CreditCard {
    var nameCreditCard:Optional<String> = Optional.empty()
    var codeCreditCard:Optional<Int> = Optional.empty()
    var maxQuotes:Optional<Short> = Optional.empty()
    var cutOff: Optional<LocalDateTime> = Optional.empty()
    var cutOffLast: Optional<LocalDateTime> = Optional.empty()
    var capital: Optional<BigDecimal> = Optional.empty()
    var capitalQuotes: Optional<BigDecimal> = Optional.empty()
    var capitalQuote: Optional<BigDecimal> = Optional.empty()

    var interest: Optional<BigDecimal> = Optional.empty()
    var interestQuote: Optional<BigDecimal> = Optional.empty()
    var interestQuotes: Optional<BigDecimal> = Optional.empty()
    var quotes: Optional<Long> = Optional.empty()
    var oneQuote:Optional<Long> = Optional.empty()
    var quotesPending:Optional<Long> = Optional.empty()
    var lastTax:Optional<Double> = Optional.empty()
    var cutoffDay:Optional<Short> = Optional.empty()
    var warningValue:Optional<BigDecimal> = Optional.empty()
}