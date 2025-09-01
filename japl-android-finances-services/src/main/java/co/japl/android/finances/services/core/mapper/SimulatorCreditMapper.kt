package co.japl.android.finances.services.core.mapper

import co.com.japl.finances.iports.dtos.SimulatorCreditDTO
import co.japl.android.finances.services.dto.CalcDTO
import co.japl.android.finances.services.enums.CalcEnum
import co.japl.android.finances.services.enums.KindOfTaxEnum

object SimulatorCreditMapper {

    fun mapper(dto: SimulatorCreditDTO): CalcDTO{
        return CalcDTO(
            dto.name!!,
            dto.value,
            dto.tax,
            dto.periods.toLong(),
            dto.quoteValue!!,
            if(dto.isCreditVariable){CalcEnum.FIX.name}else{CalcEnum.VARIABLE.name},
            dto.code,
            dto.interestValue!!,
            dto.capitalValue!!,
            KindOfTaxEnum.valueOf(dto.kindOfTax.value).name
        )
    }

    fun mapper(entity:CalcDTO):SimulatorCreditDTO{
        return SimulatorCreditDTO(
            entity.id,
            entity.name,
            entity.valueCredit,
            entity.period.toShort(),
            entity.interest,
            co.com.japl.finances.iports.enums.KindOfTaxEnum.entries.find { it.value == entity.kindOfTax}?: co.com.japl.finances.iports.enums.KindOfTaxEnum.ANUAL_EFFECTIVE,
            entity.type == CalcEnum.VARIABLE.name,
            entity.interestValue,
            entity.capitalValue,
            entity.quoteCredit
        )
    }

}
