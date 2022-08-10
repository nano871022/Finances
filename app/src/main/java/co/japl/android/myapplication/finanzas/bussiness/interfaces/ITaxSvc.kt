package co.japl.android.myapplication.bussiness.interfaces

import co.japl.android.myapplication.bussiness.DTO.TaxDTO
import java.util.*

interface ITaxSvc {
    fun get(month:Int, year:Int): Optional<TaxDTO>
}