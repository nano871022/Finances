package co.japl.finances.core.usercases.implement.paid

import co.com.japl.finances.iports.dtos.SMSPaidDTO
import co.com.japl.finances.iports.inbounds.common.ISMSRead
import co.com.japl.finances.iports.outbounds.ISMSPaidPort
import co.japl.finances.core.usercases.interfaces.paid.ISMS
import co.japl.finances.core.utils.DateUtils
import co.japl.finances.core.utils.NumbersUtil
import co.japl.finances.core.utils.SmsUtil
import java.time.LocalDateTime
import javax.inject.Inject

class SMSImpl @Inject constructor(private val svc:ISMSPaidPort, private val smsSvc:ISMSRead): ISMS {
    override fun create(dto: SMSPaidDTO): Int {
        return svc.create(dto)
    }

    override fun update(dto: SMSPaidDTO): Boolean {
        return svc.update(dto)
    }

    override fun delete(codeSMSPaidDTO: Int): Boolean {
        return svc.delete(codeSMSPaidDTO)
    }

    override fun getById(codeSMSPaidDTO: Int): SMSPaidDTO? {
        return svc.getById(codeSMSPaidDTO)
    }

    override fun validateMessagePattern(dto: SMSPaidDTO): List<String> {
        val list = mutableListOf<String>()
        smsSvc.load(dto.phoneNumber,360).takeIf{it.isNotEmpty()}?.forEach{sms->
            if(dto.pattern.isNotEmpty() && dto.pattern.toRegex().containsMatchIn(sms)){
                dto.pattern.toRegex().find(sms)?.let{
                    if(it.groupValues.size > 3){
                        list.add("OK ${it.groupValues}")
                    }else{
                        list.add("Not enough values get Name Bought, Price and Date $sms")
                    }
                }?:list.add("Not matched $sms")
            }else{
                list.add("Not matched $sms")
            }
        }
        return list
    }

    override fun getAllByCodeAccount(codeAccount: Int): List<SMSPaidDTO> {
        return svc.getByCodeAccount(codeAccount)
    }

    override fun getSmsMessages(phoneNumber:String,pattern:String,numDaysRead:Int):List<Triple<String,Double,LocalDateTime>>{
        val list = mutableListOf<Triple<String,Double,LocalDateTime>>()
        smsSvc.load(phoneNumber,numDaysRead).takeIf{it.isNotEmpty()}?.forEach{sms->
            if(pattern.isNotEmpty() && pattern.toRegex().containsMatchIn(sms)){
               pattern.toRegex().find(sms)?.let{
                   SmsUtil.getValues(it.groupValues)?.let (list::add)
                }
            }
        }
        return list
    }

    override fun enable(codeSMSPaidDTO: Int): Boolean {
        return svc.getById(codeSMSPaidDTO)?.takeIf { !it.active }?.let{
            return svc.update(it.copy(active = true))
        }?:false
    }

    override fun disable(codeSMSPaidDTO: Int): Boolean {
        return svc.getById(codeSMSPaidDTO)?.takeIf { it.active }?.let{
            return svc.update(it.copy(active = false))
        }?:false
    }
}