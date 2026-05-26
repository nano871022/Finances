package co.japl.finances.core.usercases.implement.paid

import co.com.japl.finances.iports.dtos.EmailValidationDTO
import co.com.japl.finances.iports.dtos.SMSPaidDTO
import co.com.japl.finances.iports.inbounds.common.ISMSRead
import co.com.japl.finances.iports.outbounds.ISMSPaidPort
import co.japl.finances.core.usercases.interfaces.IAccount
import co.japl.finances.core.usercases.interfaces.paid.ISMSOld
import co.japl.finances.core.usercases.interfaces.paid.ISms2
import co.japl.finances.core.utils.ExtractItemPatternUtil

import java.time.LocalDateTime
import javax.inject.Inject

class SMSImpl @Inject constructor(private val svc:ISMSPaidPort, private val smsSvc:ISMSRead, private val accountSvc:IAccount, private val paidSmsSvc:ISms2): ISMSOld {
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

    override fun validateMessagePattern(dto: SMSPaidDTO): List<EmailValidationDTO> {
        val list = mutableListOf<EmailValidationDTO>()
        smsSvc.load(dto.phoneNumber,360).takeIf{it.isNotEmpty()}?.forEach{sms->
            if(dto.pattern.isNotEmpty() && dto.pattern.toRegex().containsMatchIn(sms.first)){
                dto.pattern.toRegex().find(sms.first)?.let{
                    val values = ExtractItemPatternUtil.getValues(it.groupValues,sms.second)
                    if(values != null){
                        list.add(EmailValidationDTO(name = values.first,
                                                    value = values.second.toString(),
                                                    date = values.third.toString(),
                                                    matched = true,
                                                    bodySnippet = sms.first.take(100).replace("\n", " ")))
                    }else{
                        list.add(EmailValidationDTO(matched = false,
                                                    bodySnippet = sms.first.take(100).replace("\n", " ")))
                    }
                }?:list.add(EmailValidationDTO(matched = false,
                                                bodySnippet = sms.first.take(100).replace("\n", " ")))
            }else{
                list.add(EmailValidationDTO(matched = false,
                                            bodySnippet = sms.first.take(100).replace("\n", " ")))
            }
        }
        return list
    }

    override fun getAllByCodeAccount(codeAccount: Int): List<SMSPaidDTO> {
        return svc.getByCodeAccount(codeAccount)
    }

    override fun getSmsMessages(phoneNumber:String,pattern:String,numDaysRead:Int):List<Triple<String,Double,LocalDateTime>> =
        smsSvc.load(phoneNumber,numDaysRead).takeIf{it.isNotEmpty()}
            ?.mapNotNull{getSmsMessages(pattern,it.first,it.second)}
            ?: emptyList()



    override fun getSmsMessages(pattern: String, message: String, defaultDate:LocalDateTime): Triple<String, Double, LocalDateTime>? {
        if(pattern.isNotEmpty() && pattern.toRegex().containsMatchIn(message)){
            pattern.toRegex().find(message)?.let{
                return ExtractItemPatternUtil.getValues(it.groupValues,defaultDate)
            }
        }
        return null
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

    override fun getSmsList(phoneNumber: String): List<Pair<String, LocalDateTime>> {
        return smsSvc.load(phoneNumber, 30)
    }

    override fun read(numDaysRead: Int) {
        accountSvc.getAllActive().forEach { dto ->
            svc.getByCodeAccount(dto.id).forEach { sms ->
                getSmsMessages(sms.phoneNumber, sms.pattern, numDaysRead).forEach {
                    paidSmsSvc.createBySms(
                        co.com.japl.finances.iports.dtos.PaidDTO(
                            id = 0,
                            itemName = it.first,
                            itemValue = it.second,
                            datePaid = it.third,
                            account = dto.id,
                            recurrent = false,
                            end = LocalDateTime.now()
                        )
                    )
                }
            }
        }
    }
}
