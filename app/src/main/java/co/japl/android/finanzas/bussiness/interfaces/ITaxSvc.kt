package co.japl.android.finanzas.bussiness.interfaces

import co.japl.android.finanzas.bussiness.DTO.TaxDTO
import java.util.*

interface ITaxSvc {
    fun get(month:Int, year:Int): Optional<TaxDTO>
}