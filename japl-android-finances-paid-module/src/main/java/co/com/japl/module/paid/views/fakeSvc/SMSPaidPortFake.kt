package co.com.japl.module.paid.views.fakeSvc

import co.com.japl.finances.iports.dtos.SMSPaidDTO
import co.com.japl.finances.iports.inbounds.paid.ISMSPaidPort
import java.time.LocalDateTime

class SMSPaidPortFake : ISMSPaidPort {
    override fun create(dto: SMSPaidDTO): Int {
        return 1
    }

    override fun update(dto: SMSPaidDTO): Boolean {
        return true
    }

    override fun delete(codeSMSPaid: Int): Boolean {
        return true
    }

    override fun getById(codeSMSPaid: Int): SMSPaidDTO? {
        return null
    }

    override fun validateMessagePattern(dto: SMSPaidDTO): List<String> {
        return emptyList()
    }

    override fun getAllByCodeAccount(codeAccount: Int): List<SMSPaidDTO> {
        return emptyList()
    }

    override fun getSmsMessages(
        phoneNumber: String,
        pattern: String,
        numDaysRead: Int
    ): List<Triple<String, Double, LocalDateTime>> {
        return emptyList()
    }

    override fun enable(codeSMSPaid: Int): Boolean {
        return true
    }

    override fun disable(codeSMSPaid: Int): Boolean {
        return true
    }
}
