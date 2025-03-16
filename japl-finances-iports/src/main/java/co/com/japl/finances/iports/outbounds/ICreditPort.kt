package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.CreditDTO
import java.time.YearMonth

interface ICreditPort {
    fun getById(id:Int):CreditDTO?

    fun getAllActive(period:YearMonth):List<CreditDTO>

    fun delete(id: Int): Boolean
}