package co.com.japl.module.paid.views.sms.form.fakes

import co.com.japl.finances.iports.dtos.SMSPaidDTO
import co.com.japl.finances.iports.inbounds.paid.ISMSPaidPort

class FakeSMSPaidPort : ISMSPaidPort {
    override fun getById(code: Int): SMSPaidDTO? {
        return SMSPaidDTO(
            id = 1,
            phoneNumber = "1234567890",
            pattern = "test pattern",
            codeAccount = 1,
            nameAccount = "Test Account",
            active = true
        )
    }

    override fun getByPhoneNumber(phoneNumber: String): List<SMSPaidDTO> {
        return listOf(
            SMSPaidDTO(
                id = 1,
                phoneNumber = "1234567890",
                pattern = "test pattern 1",
                codeAccount = 1,
                nameAccount = "Test Account 1",
                active = true
            ),
            SMSPaidDTO(
                id = 2,
                phoneNumber = "1234567890",
                pattern = "test pattern 2",
                codeAccount = 2,
                nameAccount = "Test Account 2",
                active = false
            )
        )
    }

    override fun getByAccount(codeAccount: Int): List<SMSPaidDTO> {
        return getByPhoneNumber("1234567890")
    }

    override fun validateMessagePattern(dto: SMSPaidDTO): List<String> {
        return listOf("Message 1", "Message 2")
    }

    override fun create(dto: SMSPaidDTO): Int {
        return 1
    }

    override fun update(dto: SMSPaidDTO): Boolean {
        return true
    }

    override fun delete(id: Int): Boolean {
        return true
    }

    override fun get(): List<SMSPaidDTO> {
        return getByPhoneNumber("1234567890")
    }

    override fun save(dto: SMSPaidDTO): Int {
        return 1
    }

    override fun get(id: Int): SMSPaidDTO? {
        return getById(id)
    }
}
