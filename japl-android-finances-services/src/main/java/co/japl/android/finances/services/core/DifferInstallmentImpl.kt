package co.japl.android.finances.services.core

import co.japl.android.finances.services.core.mapper.DifferInstallmentMapper
import co.japl.android.finances.services.interfaces.IDifferInstallment
import co.japl.finances.core.adapters.outbound.interfaces.IDifferInstallmentRecapPort
import co.japl.finances.core.dto.DifferInstallmentDTO
import java.time.LocalDate
import javax.inject.Inject

class DifferInstallmentImpl @Inject constructor(private val differInstallmentSvc:IDifferInstallment):IDifferInstallmentRecapPort {
    override fun get(cutOff: LocalDate): List<DifferInstallmentDTO> {
        return differInstallmentSvc.get(cutOff).map (DifferInstallmentMapper::mapper)
    }

    override fun get(id: Int): DifferInstallmentDTO? {
        val differ = differInstallmentSvc.get(id)
            if (differ.isPresent) {
             return DifferInstallmentMapper.mapper(differ.get())
            }
        return null
    }
}