package co.japl.finances.core.adapters.inbound.implement.account

import co.com.japl.finances.iports.dtos.AccountDTO
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort
import co.japl.finances.core.usercases.interfaces.IAccount
import javax.inject.Inject

class AccountPort @Inject constructor(private val service : IAccount): IAccountPort {
    override fun getById(codeAccount: Int): AccountDTO? {
        return service.getById(codeAccount)
    }

    override fun getAll(): List<AccountDTO> {
        return service.getAll()
    }

    override fun getAllActive(): List<AccountDTO> {
       return service.getAllActive().takeIf { it.isNotEmpty() }?.filter { it.active }?: emptyList()
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
}