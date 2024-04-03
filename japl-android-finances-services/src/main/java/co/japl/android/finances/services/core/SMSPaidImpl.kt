package co.japl.android.finances.services.core

import co.com.japl.finances.iports.dtos.SMSPaidDTO
import co.com.japl.finances.iports.outbounds.ISMSPaidPort
import co.japl.android.finances.services.core.mapper.SmsPaidMapper
import co.japl.android.finances.services.dao.interfaces.ISmsPaidDAO
import co.japl.android.finances.services.entities.SmsPaid
import javax.inject.Inject

class SMSPaidImpl @Inject constructor(private val svc:ISmsPaidDAO): ISMSPaidPort {
    override fun create(dto: SMSPaidDTO): Int {
        return svc.save(SmsPaidMapper.mapping(dto)).toInt()
    }

    override fun update(dto: SMSPaidDTO): Boolean {
        return svc.save(SmsPaidMapper.mapping(dto))>0
    }

    override fun delete(codeSMSPaidDTO: Int): Boolean {
        return svc.delete(codeSMSPaidDTO)
    }

    override fun getById(codeSMSPaidDTO: Int): SMSPaidDTO? {
        return svc.get(codeSMSPaidDTO).takeIf { it.isPresent }?.let {SmsPaidMapper.mapping(it.get())}
    }

    override fun getByCodeAccount(codeAccount: Int): List<SMSPaidDTO> {
        require(codeAccount > 0)
        val entity = SmsPaid(codeAccount = codeAccount)
        return svc.get(values=entity).map { SmsPaidMapper.mapping(it) }
    }

}