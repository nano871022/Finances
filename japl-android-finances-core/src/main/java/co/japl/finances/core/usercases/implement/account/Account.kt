package co.japl.finances.core.usercases.implement.account

import co.com.japl.finances.iports.dtos.AccountDTO
import co.com.japl.finances.iports.outbounds.IAccountPort
import co.japl.finances.core.usercases.interfaces.IAccount
import javax.inject.Inject

class Account @Inject constructor(private val service : IAccountPort) : IAccount {
    override fun getById(codeAccount: Int): AccountDTO? {
        return service.getById(codeAccount)
    }

    override fun getAll(): List<AccountDTO> {
        return service.getAll()
    }

    override fun delete(codeAccount: Int): Boolean {
        return service.delete(codeAccount)
    }

    override fun create(dto: AccountDTO): Int {
        return service.create(dto)
    }

    override fun update(dto: AccountDTO): Boolean {
        return service.update(dto)
    }

    override fun getAllActive(): List<AccountDTO> {
        return service.getAll().takeIf { it.isNotEmpty() }?.filter { it.active }?: emptyList()
    }
}