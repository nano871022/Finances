package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.PaidDTO
import java.time.YearMonth

interface IPaidPort {

    fun getActivePaid(codeAccount: Int, period: YearMonth):List<PaidDTO>

    fun get(codePaid:Int): PaidDTO?

    fun create(paid: PaidDTO):Int

    fun update(paid: PaidDTO):Boolean

    fun delete(codePaid:Int):Boolean
}