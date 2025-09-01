package co.japl.android.finances.services.core

import co.com.japl.finances.iports.dtos.ExtraValueAmortizationDTO
import co.com.japl.finances.iports.outbounds.IExtraValueAmortizationPort
import co.japl.android.finances.services.dao.implement.AddAmortizationMapper
import co.japl.android.finances.services.dao.interfaces.IAddAmortizationDAO
import javax.inject.Inject

class ExtraValueAmortizationImpl @Inject constructor(private val dao: IAddAmortizationDAO) : IExtraValueAmortizationPort {

    override fun save(dto: ExtraValueAmortizationDTO):Long{
        val entity = AddAmortizationMapper.mapper(dto)
        return dao.save(entity)
    }

    override fun getByCode(code:Int):List<ExtraValueAmortizationDTO>{
        return dao.getAll(code).map(AddAmortizationMapper::mapper)
    }

}