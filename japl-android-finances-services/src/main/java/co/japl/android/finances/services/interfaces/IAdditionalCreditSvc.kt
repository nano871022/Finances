package co.japl.android.finances.services.interfaces

import co.japl.android.finances.services.dto.AdditionalCreditDTO
import java.math.BigDecimal
import java.time.LocalDate

interface IAdditionalCreditSvc : SaveSvc<AdditionalCreditDTO>,ISaveSvc<AdditionalCreditDTO>{
    fun get(id: Long): List<AdditionalCreditDTO>
    fun updateValue(id:Int,value:BigDecimal):Boolean
    fun get(codeCredit:Int, date: LocalDate): List<AdditionalCreditDTO>
}