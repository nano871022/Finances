package co.japl.android.myapplication.finanzas.pojo

import java.math.BigDecimal
import java.util.*

class QuoteCreditCard {
    lateinit var value:Optional<BigDecimal>
    lateinit var period:Optional<Long>
    lateinit var tax:Optional<Double>
    lateinit var response:Optional<BigDecimal>
    lateinit var name:Optional<String>
}