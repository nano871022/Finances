package co.japl.android.myapplication.bussiness.interfaces

import co.japl.android.myapplication.bussiness.DTO.TaxDTO
import co.japl.android.myapplication.finanzas.enums.TaxEnum
import java.util.*

interface ITaxSvc {
    fun get(codCreditCard:Long,month:Int, year:Int, kind: TaxEnum = TaxEnum.CREDIT_CARD): Optional<TaxDTO>
}