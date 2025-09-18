package co.com.japl.module.paid.views.accounts.form.fakes

import co.com.japl.finances.iports.dtos.AccountDTO
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import java.time.LocalDate

class FakeAccountPort : IAccountPort {
    override fun getById(code: Int): AccountDTO? {
        return AccountDTO(
            id = 1,
            name = "Test Account",
            active = true,
            create = LocalDate.now()
        )
    }

    override fun getAll(): List<AccountDTO> {
        return listOf(
            AccountDTO(
                id = 1,
                name = "Test Account 1",
                active = true,
                create = LocalDate.now()
            ),
            AccountDTO(
                id = 2,
                name = "Test Account 2",
                active = false,
                create = LocalDate.now()
            )
        )
    }

    override fun create(dto: AccountDTO): Int {
        return 1
    }

    override fun update(dto: AccountDTO): Boolean {
        return true
    }

    override fun delete(id: Int): Boolean {
        return true
    }

    override fun get(id: Int): AccountDTO? {
        return getById(id)
    }

    override fun get(): List<AccountDTO> {
        return getAll()
    }

    override fun save(dto: AccountDTO): Int {
        return 1
    }
}
