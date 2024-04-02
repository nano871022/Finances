package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.PaidDTO
import java.math.BigDecimal
import java.time.YearMonth

interface IPaidRecapPort {

    fun getTotalPaid(): BigDecimal

}