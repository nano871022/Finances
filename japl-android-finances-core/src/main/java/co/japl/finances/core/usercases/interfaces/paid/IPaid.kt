package co.japl.finances.core.usercases.interfaces.paid

import co.com.japl.finances.iports.dtos.PaidDTO
import co.com.japl.finances.iports.dtos.PaidRecapDTO
import java.time.YearMonth

interface IPaid {
    fun get(codeAccount:Int, period: YearMonth):List<PaidDTO>

    fun getRecap(codeAccount:Int,period: YearMonth): PaidRecapDTO?

    fun getListGraph(codeAccount:Int,period:YearMonth):List<Pair<String,Double>>

    fun get(codePaid:Int):PaidDTO?

    fun create(paid:PaidDTO):Int

    fun update(paid:PaidDTO):Boolean

    fun delete(id:Int):Boolean
}