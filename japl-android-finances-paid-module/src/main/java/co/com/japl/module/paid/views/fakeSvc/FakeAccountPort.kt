package co.com.japl.module.paid.views.fakeSvc

import co.com.japl.finances.iports.dtos.AccountDTO
import co.com.japl.finances.iports.inbounds.inputs.IAccountPort

class FakeAccountPort : IAccountPort {
    override fun getById(codeAccount: Int): AccountDTO? {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<AccountDTO> {
        TODO("Not yet implemented")
    }

    override fun getAllActive(): List<AccountDTO> {
        TODO("Not yet implemented")
    }

    override fun delete(codeAccount: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun create(dto: AccountDTO): Int {
        TODO("Not yet implemented")
    }

    override fun update(dto: AccountDTO): Boolean {
        TODO("Not yet implemented")
    }

}