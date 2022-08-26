package co.japl.android.myapplication.bussiness.interfaces

import co.japl.android.myapplication.bussiness.DTO.TaxDTO
import co.japl.android.myapplication.finanzas.utils.TaxEnum
import java.util.*

interface ITaxSvc {
    fun get(month:Int, year:Int, kind:TaxEnum = TaxEnum.CREDIT_CARD): Optional<TaxDTO>
}