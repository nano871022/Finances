package co.com.japl.module.paid.views.paid.form.fakes

import co.com.japl.finances.iports.dtos.PaidDTO
import co.com.japl.finances.iports.inbounds.paid.IPaidPort
import java.math.BigDecimal
import java.time.LocalDateTime

class FakePaidPort : IPaidPort {
    override fun get(codePaid: Int): PaidDTO? {
        return PaidDTO(
            id = 1,
            datePaid = LocalDateTime.now(),
            itemName = "Test Paid",
            itemValue = 100.0,
            recurrent = false,
            account = 1,
            end = LocalDateTime.now()
        )
    }

    override fun get(codeAccount: Int, date: LocalDateTime): List<PaidDTO> {
        return listOf(
            PaidDTO(
                id = 1,
                datePaid = LocalDateTime.now(),
                itemName = "Test Paid 1",
                itemValue = 100.0,
                recurrent = false,
                account = 1,
                end = LocalDateTime.now()
            ),
            PaidDTO(
                id = 2,
                datePaid = LocalDateTime.now(),
                itemName = "Test Paid 2",
                itemValue = 200.0,
                recurrent = true,
                account = 1,
                end = LocalDateTime.now()
            )
        )
    }

    override fun get(codeAccount: Int, month: Int, year: Int, kindOf: Short?): List<PaidDTO> {
        return get(codeAccount, LocalDateTime.now())
    }

    override fun getRecurrent(codeAccount: Int): List<PaidDTO> {
        return get(codeAccount, LocalDateTime.now()).filter { it.recurrent }
    }

    override fun create(dto: PaidDTO): Int {
        return 1
    }

    override fun update(dto: PaidDTO): Boolean {
        return true
    }

    override fun delete(codePaid: Int): Boolean {
        return true
    }

    override fun get(id: Int): PaidDTO? {
        return get(id, LocalDateTime.now()).firstOrNull()
    }

    override fun get(): List<PaidDTO> {
        return get(1, LocalDateTime.now())
    }

    override fun save(dto: PaidDTO): Int {
        return 1
    }
}
