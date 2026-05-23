package co.japl.android.finances.services.core.mapper

import co.com.japl.finances.iports.dtos.AccountDTO

object AccountMapper {

    fun mapper(account: co.japl.android.finances.services.dto.AccountDTO): AccountDTO {
        return AccountDTO(
            id = account.id,
            name = account.name,
            active = account.active,
            create = account.create
        )
    }

    fun mapper(account: AccountDTO):co.japl.android.finances.services.dto.AccountDTO {
        return co.japl.android.finances.services.dto.AccountDTO(
            id = account.id,
            name = account.name,
            active = account.active,
            create = account.create
        )
    }
}