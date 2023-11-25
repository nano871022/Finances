package co.japl.finances.core.usercases.implement.recap

import co.japl.finances.core.adapters.outbound.interfaces.IPaidRecapPort
import co.japl.finances.core.usercases.interfaces.recap.IPaid
import java.math.BigDecimal
import javax.inject.Inject

class PaidImp @Inject constructor(private val paidImpl:IPaidRecapPort): IPaid {
    override fun getTotalPaid(): BigDecimal {
        return paidImpl.getTotalPaid()
    }
}