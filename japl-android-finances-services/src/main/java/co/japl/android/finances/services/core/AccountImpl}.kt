package co.japl.android.finances.services.core

import co.com.japl.finances.iports.dtos.AccountDTO
import co.com.japl.finances.iports.outbounds.IAccountPort
import co.japl.android.finances.services.core.mapper.AccountMapper
import co.japl.android.finances.services.dao.interfaces.IAccountSvc
import java.util.Optional
import javax.inject.Inject

class AccountImpl @Inject constructor(private val service:IAccountSvc) : IAccountPort {
    override fun getById(codeAccount: Int): AccountDTO? {
        require(codeAccount > 0) { "Code Account must be greater than zero" }
        return service.get(codeAccount)?.takeIf { it.isPresent }?.
            let { account ->
                AccountMapper.mapper(account.get())
            }
    }

    override fun getAll(): List<AccountDTO> {
        return service.getAll().map(AccountMapper::mapper)
    }

    override fun delete(codeAccount: Int): Boolean {
        require(codeAccount > 0) { "Code Account must be greater than zero" }
        return service.delete(codeAccount)
    }

    override fun create(dto: AccountDTO): Int {
        require(dto.id == 0) { "Code Account must be equal zero" }
        return service.save(dto.let { AccountMapper.mapper(it)}).toInt()
    }

    override fun update(dto: AccountDTO): Boolean {
        require(dto.id > 0) { "Code Account must be greater than zero" }
        return service.save(dto.let { AccountMapper.mapper(it) }) > 0
    }
}