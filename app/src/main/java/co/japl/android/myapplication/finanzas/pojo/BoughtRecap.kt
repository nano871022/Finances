package co.japl.android.myapplication.finanzas.pojo

import com.google.android.material.textfield.TextInputEditText
import java.math.BigDecimal
import java.util.*

class BoughtRecap {
    lateinit var capitalValue:Optional<BigDecimal>
    lateinit var interestValue:Optional<BigDecimal>
    lateinit var totalValue:Optional<BigDecimal>
    lateinit var pendingToPay:Optional<BigDecimal>
    lateinit var currentValueCapital: Optional<BigDecimal>
    lateinit var quotesValueCapital: Optional<BigDecimal>
    lateinit var currentValueInterest: Optional<BigDecimal>
    lateinit var quotesValueInterest: Optional<BigDecimal>

    lateinit var totalItem: Optional<Int>
    lateinit var recurrentItem: Optional<Int>
    lateinit var quotesItem: Optional<Int>
    lateinit var quoteItem: Optional<Int>

}