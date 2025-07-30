package co.japl.finances.core.usercases.interfaces.credit

import co.com.japl.finances.iports.dtos.GracePeriodDTO
import javax.inject.Inject

interface IPeriodGrace {

    fun add(dto: GracePeriodDTO): Boolean

    fun delete(codeCredit: Int): Boolean

    fun hasGracePeriod(codeCredit: Int):Boolean

    fun getList(codeCredit: Int):List<GracePeriodDTO>
}