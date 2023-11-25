package co.japl.android.finances.services.core

import co.japl.android.finances.services.interfaces.IPaidSvc
import co.japl.finances.core.adapters.outbound.interfaces.IPaidRecapPort
import java.math.BigDecimal
import javax.inject.Inject

class PaidImpl @Inject constructor(private val paidImpl:IPaidSvc) : IPaidRecapPort {
    override fun getTotalPaid(): BigDecimal {
        return paidImpl.getTotalPaid()
    }

}