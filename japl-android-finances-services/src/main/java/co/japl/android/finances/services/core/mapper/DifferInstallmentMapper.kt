package co.japl.android.finances.services.core.mapper

import co.japl.android.finances.services.dto.DifferInstallmentDTO

object DifferInstallmentMapper {

    fun mapper(differInstallmentDTO: DifferInstallmentDTO):co.com.japl.finances.iports.dtos.DifferInstallmentDTO{
        return co.com.japl.finances.iports.dtos.DifferInstallmentDTO(
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