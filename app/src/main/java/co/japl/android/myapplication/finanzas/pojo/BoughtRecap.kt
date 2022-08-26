package co.japl.android.myapplication.finanzas.pojo

import java.math.BigDecimal
import java.util.*

class BoughtRecap {
    lateinit var capitalValue:Optional<BigDecimal>
    lateinit var interestValue:Optional<BigDecimal>
    lateinit var totalValue:Optional<BigDecimal>
    lateinit var pendingToPay:Optional<BigDecimal>
}