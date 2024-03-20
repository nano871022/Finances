package co.japl.finances.core.usercases.implement.creditcard

import co.com.japl.finances.iports.dtos.SMSCreditCard
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.inbounds.common.ISMSRead
import co.com.japl.finances.iports.outbounds.ISMSCreditCardPort
import co.japl.finances.core.usercases.interfaces.creditcard.ISMSCreditCard
import co.japl.finances.core.utils.DateUtils
import co.japl.finances.core.utils.NumbersUtil
import java.time.LocalDateTime
import javax.inject.Inject

class SMSCreditCardImpl @Inject constructor(private val svc:ISMSCreditCardPort, private val smsSvc:ISMSRead): ISMSCreditCard {
    override fun create(dto: SMSCreditCard): Int {
        return svc.create(dto)
    }

    override fun update(dto: SMSCreditCard): Boolean {
        return svc.update(dto)
    }

    override fun delete(codeSMSCreditCard: Int): Boolean {
        return svc.delete(codeSMSCreditCard)
    }

    override fun getById(codeSMSCreditCard: Int): SMSCreditCard? {
        return svc.getById(codeSMSCreditCard)
    }

    override fun validateMessagePattern(dto: SMSCreditCard): List<String> {
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

    override fun getAllByCodeCreditCard(codeCreditCard: Int): List<SMSCreditCard> {
        return svc.getByCodeCreditCard(codeCreditCard)
    }

    override fun getSmsMessages(phoneNumber:String,pattern:String,numDaysRead:Int):List<Triple<String,Double,LocalDateTime>>{
        val list = mutableListOf<Triple<String,Double,LocalDateTime>>()
        smsSvc.load(phoneNumber,numDaysRead).takeIf{it.isNotEmpty()}?.forEach{sms->
            if(pattern.isNotEmpty() && pattern.toRegex().containsMatchIn(sms)){
               pattern.toRegex().find(sms)?.let{mr->
                    if(mr.groupValues.size > 3){
                        val name = mr.groupValues[2]
                        val value = mr.groupValues[1].takeIf { it != null && NumbersUtil.isNumberRegex(it) }?.let(NumbersUtil::toDoubleOrZero) ?: 0.0
                        val date = mr.groupValues[3].takeIf { it != null && DateUtils.isLocalDateRegex(it) }?.let(DateUtils::toLocalDateTime)?:LocalDateTime.now()
                        list.add(Triple(name,value,date))
                    }
                }
            }
        }
        return list
    }

    override fun enable(codeSMSCreditCard: Int): Boolean {
        return svc.getById(codeSMSCreditCard)?.takeIf { !it.active }?.let{
            return svc.update(it.copy(active = true))
        }?:false
    }

    override fun disable(codeSMSCreditCard: Int): Boolean {
        return svc.getById(codeSMSCreditCard)?.takeIf { it.active }?.let{
            return svc.update(it.copy(active = false))
        }?:false
    }

    override fun getByCreditCardAndKindInterest(
        codeCreditCard: Int,
        kind: KindInterestRateEnum
    ): List<SMSCreditCard> {
        return svc.getByCreditCardAndKindInterest(codeCreditCard,kind)
    }
}