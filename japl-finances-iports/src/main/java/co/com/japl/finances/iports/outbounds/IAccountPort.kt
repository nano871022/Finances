package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.AccountDTO

interface IAccountPort {

    fun getById(codeAccount:Int): AccountDTO?

    fun getAll():List<AccountDTO>

    fun delete(codeAccount: Int):Boolean

    fun create(dto: AccountDTO):Int

    fun update(dto: AccountDTO):Boolean

}