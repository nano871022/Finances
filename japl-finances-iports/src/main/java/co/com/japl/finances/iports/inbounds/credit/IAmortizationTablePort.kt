package co.com.japl.finances.iports.inbounds.credit

import android.R
import co.com.japl.finances.iports.dtos.AmortizationRowDTO
import co.com.japl.finances.iports.enums.KindAmortization

interface IAmortizationTablePort {

    fun getAmortization(code:Int,kind: KindAmortization,cache: Boolean):List<AmortizationRowDTO>

}