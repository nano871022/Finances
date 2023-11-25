package co.japl.android.finances.services.interfaces

import co.japl.android.finances.services.dto.AccountDTO

interface IAccountSvc : SaveSvc<AccountDTO>,ISaveSvc<AccountDTO> {
}