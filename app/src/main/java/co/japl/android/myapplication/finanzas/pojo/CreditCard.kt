package co.japl.android.myapplication.pojo

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

class CreditCard {
    lateinit var nameCreditCard:Optional<String>
    lateinit var codeCreditCard:Optional<Int>
    lateinit var cutOff: Optional<LocalDateTime>
    lateinit var capital: Optional<BigDecimal>
    lateinit var capitalQuotes: Optional<BigDecimal>
    lateinit var capitalQuote: Optional<BigDecimal>
    lateinit var interest: Optional<BigDecimal>
    lateinit var interestQuote: Optional<BigDecimal>
    lateinit var interestQuotes: Optional<BigDecimal>
    lateinit var quotes: Optional<Long>
    lateinit var oneQuote:Optional<Long>
    lateinit var quotesPending:Optional<Long>
    lateinit var lastTax:Optional<Double>
    lateinit var cutoffDay:Optional<Short>
}