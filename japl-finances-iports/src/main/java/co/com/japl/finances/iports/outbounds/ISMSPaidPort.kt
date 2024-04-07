package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.SMSPaidDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum

interface ISMSPaidPort {

    fun create(dto:SMSPaidDTO):Int

    fun update(dto:SMSPaidDTO):Boolean

    fun delete(codeSMSPaidDTO: Int):Boolean

    fun getById(codeSMSPaidDTO: Int):SMSPaidDTO?

    fun getByCodeAccount(codeAccount: Int):List<SMSPaidDTO>

}