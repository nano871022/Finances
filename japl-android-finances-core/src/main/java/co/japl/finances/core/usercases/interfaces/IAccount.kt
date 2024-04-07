package co.japl.finances.core.usercases.interfaces

import co.com.japl.finances.iports.dtos.AccountDTO

interface IAccount {


    fun getById(codeAccount:Int): AccountDTO?

    fun getAll():List<AccountDTO>

    fun delete(codeAccount: Int):Boolean

    fun create(dto: AccountDTO):Int

    fun update(dto: AccountDTO):Boolean
    fun getAllActive(): List<AccountDTO>
}