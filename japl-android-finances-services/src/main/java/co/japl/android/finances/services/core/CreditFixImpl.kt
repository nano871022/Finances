package co.japl.android.finances.services.core

import co.japl.android.finances.services.core.mapper.CreditMapper
import co.japl.android.finances.services.interfaces.ICreditFix
import co.com.japl.finances.iports.outbounds.ICreditFixRecapPort
import co.com.japl.finances.iports.dtos.CreditDTO
import javax.inject.Inject

class CreditFixImpl @Inject constructor(private val creditFix:ICreditFix):ICreditFixRecapPort {
    override fun getAll(): List<CreditDTO> {
        return creditFix.getAll().map (CreditMapper::mapper)
    }
}