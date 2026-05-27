package co.japl.finances.core.usercases.implement.creditcard

import co.com.japl.finances.iports.dtos.EmailValidationDTO
import co.com.japl.finances.iports.dtos.SMSCreditCard
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.inbounds.common.ISMSRead
import co.com.japl.finances.iports.outbounds.ISMSCreditCardPort
import co.japl.finances.core.enums.AutoLoadKind
import co.japl.finances.core.usercases.interfaces.common.ICreditCard
import co.japl.finances.core.usercases.interfaces.creditcard.ISMSCreditCard
import co.japl.finances.core.usercases.interfaces.creditcard.bought.lists.IBoughtSms
import co.japl.finances.core.utils.ExtractItemPatternUtil
import java.time.LocalDateTime
import javax.inject.Inject

class SMSCreditCardImpl @Inject constructor(private val svc:ISMSCreditCardPort, private val smsSvc:ISMSRead, private val creditCardSvc: ICreditCard, private val boughtSmsSvc: IBoughtSms): ISMSCreditCard {
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

    override fun validateMessagePattern(dto: SMSCreditCard): List<EmailValidationDTO> {
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

    override fun getAllByCodeCreditCard(codeCreditCard: Int): List<SMSCreditCard> {
        return svc.getByCodeCreditCard(codeCreditCard)
    }

    override fun getSmsMessages(phoneNumber:String,pattern:String,numDaysRead:Int):List<Triple<String,Double,LocalDateTime>> =
        smsSvc.load(phoneNumber,numDaysRead).takeIf{it.isNotEmpty()}
            ?.mapNotNull{getSmsMessages(pattern,it.first,it.second)}
            ?: emptyList()


    override fun getSmsMessages(pattern: String,sms:String, defaultDate:LocalDateTime):Triple<String,Double,LocalDateTime>?{
        if(pattern.isNotEmpty() && pattern.toRegex().containsMatchIn(sms)){
            pattern.toRegex().find(sms)?.let{
                return ExtractItemPatternUtil.getValues(it.groupValues,defaultDate)
            }
        }
        return null
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

    override fun getSmsList(phoneNumber: String): List<Pair<String,LocalDateTime>> {
        return smsSvc.load(phoneNumber, 30)
    }

    override fun getByCreditCardAndKindInterest(
        codeCreditCard: Int,
        kind: KindInterestRateEnum
    ): List<SMSCreditCard> {
        return svc.getByCreditCardAndKindInterest(codeCreditCard,kind)
    }

    override fun read(numDaysRead: Int) {
        creditCardSvc.getAll().forEach { ccDto ->
            listOf(
                KindInterestRateEnum.CREDIT_CARD,
                KindInterestRateEnum.CASH_ADVANCE,
                KindInterestRateEnum.WALLET_BUY
            ).forEach { kind ->
                svc.getByCreditCardAndKindInterest(ccDto.id, kind).forEach { sms ->
                    getSmsMessages(sms.phoneNumber, sms.pattern, numDaysRead).forEach {
                        boughtSmsSvc.createByAutoLoad(
                            co.com.japl.finances.iports.dtos.CreditCardBoughtDTO(
                                id = 0,
                                nameItem = it.first,
                                valueItem = it.second.toBigDecimal(),
                                boughtDate = it.third,
                                codeCreditCard = ccDto.id,
                                kind = kind,
                                endDate = LocalDateTime.MAX,
                                cutOutDate = LocalDateTime.now(),
                                createDate = LocalDateTime.now(),
                                interest = 0.0,
                                kindOfTax = co.com.japl.finances.iports.enums.KindOfTaxEnum.MONTHLY_EFFECTIVE,
                                month = 1,
                                nameCreditCard = "",
                                recurrent = 0
                            ),
                            AutoLoadKind.SMS
                        )
                    }
                }
            }
        }
    }
}
