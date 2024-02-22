package co.japl.android.finances.services.dao.interfaces

import co.japl.android.finances.services.dto.AccountDTO
import co.japl.android.finances.services.interfaces.ISaveSvc
import co.japl.android.finances.services.interfaces.SaveSvc

interface IAccountSvc : SaveSvc<AccountDTO>, ISaveSvc<AccountDTO> {
}