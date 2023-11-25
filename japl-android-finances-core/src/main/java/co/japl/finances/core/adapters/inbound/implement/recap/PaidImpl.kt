package co.japl.finances.core.adapters.inbound.implement.recap

import co.japl.finances.core.adapters.inbound.interfaces.recap.IPaidPort
import co.japl.finances.core.usercases.interfaces.recap.IPaid
import java.math.BigDecimal
import javax.inject.Inject

class PaidImpl @Inject constructor(private val paidImpl: IPaid):IPaidPort {
    override fun getTotalPaid(): BigDecimal {
        return paidImpl.getTotalPaid()
    }

}