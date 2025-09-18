package co.com.japl.module.paid.views.fakeSvc

import co.com.japl.finances.iports.dtos.AccountDTO
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort

class AccountPortFake : IAccountPort {
    override fun getById(codeAccount: Int): AccountDTO? {
        return null
    }

    override fun getAll(): List<AccountDTO> {
        return emptyList()
    }

    override fun getAllActive(): List<AccountDTO> {
        return emptyList()
    }

    override fun delete(codeAccount: Int): Boolean {
        return true
    }

    override fun create(dto: AccountDTO): Int {
        return 1
    }

    override fun update(dto: AccountDTO): Boolean {
        return true
    }
}
