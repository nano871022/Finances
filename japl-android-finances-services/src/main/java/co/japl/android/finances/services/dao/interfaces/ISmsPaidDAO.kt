package co.japl.android.finances.services.dao.interfaces

import co.japl.android.finances.services.entities.SmsPaid
import co.japl.android.finances.services.interfaces.ISaveSvc
import co.japl.android.finances.services.interfaces.SaveSvc

interface ISmsPaidDAO : SaveSvc<SmsPaid>, ISaveSvc<SmsPaid> {
    fun getByAccount(codeAccount:Int):List<SmsPaid>
}