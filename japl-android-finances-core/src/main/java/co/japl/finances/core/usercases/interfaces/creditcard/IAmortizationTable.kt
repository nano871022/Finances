package co.japl.finances.core.usercases.interfaces.creditcard

import co.com.japl.finances.iports.dtos.AmortizationRowDTO
import co.com.japl.finances.iports.enums.KindAmortization

interface IAmortizationTable {
    fun getAmortization(code: Int,kind: KindAmortization,cache: Boolean):List<AmortizationRowDTO>
}