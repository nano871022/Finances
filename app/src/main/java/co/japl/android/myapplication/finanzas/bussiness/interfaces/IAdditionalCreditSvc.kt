package co.japl.android.myapplication.finanzas.bussiness.interfaces

import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.AdditionalCreditDTO
import java.math.BigDecimal
import java.time.LocalDate
import java.util.Optional

interface IAdditionalCreditSvc : SaveSvc<AdditionalCreditDTO>,ISaveSvc<AdditionalCreditDTO>{
    fun get(id: Long): List<AdditionalCreditDTO>
    fun updateValue(id:Int,value:BigDecimal):Boolean
    fun get(codeCredit:Int, date: LocalDate): List<AdditionalCreditDTO>
}