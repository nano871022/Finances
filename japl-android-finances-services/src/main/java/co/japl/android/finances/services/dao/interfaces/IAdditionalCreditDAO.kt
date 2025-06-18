package co.japl.android.finances.services.dao.interfaces

import co.japl.android.finances.services.dto.AdditionalCreditDTO
import co.japl.android.finances.services.interfaces.ISaveSvc
import co.japl.android.finances.services.interfaces.SaveSvc
import java.math.BigDecimal
import java.time.LocalDate

interface IAdditionalCreditDAO : SaveSvc<AdditionalCreditDTO>, ISaveSvc<AdditionalCreditDTO> {
    fun get(id: Long): List<AdditionalCreditDTO>
    fun updateValue(id:Int,value:BigDecimal):Boolean
    fun get(codeCredit:Int, date: LocalDate): List<AdditionalCreditDTO>
    fun update(dto:AdditionalCreditDTO):Int
}