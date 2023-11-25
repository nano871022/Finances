package co.japl.android.finances.services.core.mapper

import co.japl.android.finances.services.dto.DifferInstallmentDTO

object DifferInstallmentMapper {

    fun mapper(differInstallmentDTO: DifferInstallmentDTO):co.japl.finances.core.dto.DifferInstallmentDTO{
        return co.japl.finances.core.dto.DifferInstallmentDTO(
            differInstallmentDTO.id,
            differInstallmentDTO.create,
            differInstallmentDTO.cdBoughtCreditCard,
            differInstallmentDTO.pendingValuePayable,
            differInstallmentDTO.originValue,
            differInstallmentDTO.newInstallment,
            differInstallmentDTO.oldInstallment
        )
    }
}