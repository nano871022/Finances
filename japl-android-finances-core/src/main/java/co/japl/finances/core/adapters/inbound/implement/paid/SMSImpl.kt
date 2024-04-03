package co.japl.finances.core.adapters.inbound.implement.paid

import co.com.japl.finances.iports.dtos.PaidDTO
import co.com.japl.finances.iports.dtos.SMSPaidDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.com.japl.finances.iports.inbounds.paid.ISMSPaidPort
import co.com.japl.finances.iports.inbounds.paid.ISmsPort
import co.japl.finances.core.usercases.interfaces.paid.ISMS
import co.japl.finances.core.usercases.interfaces.paid.ISms
import java.time.LocalDateTime
import javax.inject.Inject

class SMSImpl @Inject constructor(private val svc:ISMS, private val smsSvc:ISms) : ISMSPaidPort , ISmsPort{
    override fun create(dto: SMSPaidDTO): Int {
        require(dto.id == 0){"Id must be zero"}
        return svc.create(dto)
    }

    override fun update(dto: SMSPaidDTO): Boolean {
        require(dto.id != 0){"Id must not be zero"}
        return svc.update(dto)
    }

    override fun delete(codeSMSPaidDTO: Int): Boolean {
        require(codeSMSPaidDTO != 0){"Id must not be zero"}
        return svc.delete(codeSMSPaidDTO)
    }

    override fun getById(codeSMSPaidDTO: Int): SMSPaidDTO? {
        require(codeSMSPaidDTO != 0){"Id must not be zero"}
        return svc.getById(codeSMSPaidDTO)
    }

    override fun validateMessagePattern(dto: SMSPaidDTO): List<String> {
        return svc.validateMessagePattern(dto)
    }

    override fun getAllByCodeAccount(codeCreditCard: Int): List<SMSPaidDTO> {
        return svc.getAllByCodeAccount(codeCreditCard)
    }

    override fun getSmsMessages(
        phoneNumber: String,
        pattern: String,numDaysRead:Int
    ): List<Triple<String, Double, LocalDateTime>> {
        require(phoneNumber.isNotEmpty()){"Phone number must not be empty"}
        require(pattern.isNotEmpty()){"Pattern must not be empty"}
        return svc.getSmsMessages(phoneNumber,pattern,numDaysRead)
    }

    override fun enable(codeSMSPaidDTO: Int): Boolean {
        return svc.enable(codeSMSPaidDTO)
    }

    override fun disable(codeSMSPaidDTO: Int): Boolean {
        return svc.disable(codeSMSPaidDTO)
    }

    override fun createBySms(name: String, value: Double, date: LocalDateTime,codeAccount:Int) {
        smsSvc.createBySms(
            PaidDTO(
                id=0,
                itemName =name,
                itemValue = value,
                datePaid=date,
                account=codeAccount,
                recurrent= false,
                end= LocalDateTime.now()
            )
        )
    }
}