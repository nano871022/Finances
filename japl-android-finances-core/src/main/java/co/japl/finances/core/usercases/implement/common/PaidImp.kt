package co.japl.finances.core.usercases.implement.common

import co.com.japl.finances.iports.outbounds.IPaidRecapPort
import co.japl.finances.core.usercases.interfaces.common.IPaid
import java.math.BigDecimal
import javax.inject.Inject

class PaidImp @Inject constructor(private val paidImpl:IPaidRecapPort): IPaid {
    override fun getTotalPaid(): BigDecimal {
        return paidImpl.getTotalPaid()
    }
}