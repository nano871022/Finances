package co.com.japl.finances.iports.outbounds

import java.math.BigDecimal

interface IPaidRecapPort {

    fun getTotalPaid(): BigDecimal
}