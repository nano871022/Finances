package co.japl.android.myapplication.finanzas.bussiness.interfaces

import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidDTO
import java.math.BigDecimal
import java.time.LocalDate

interface IPaidSvc: SaveSvc<PaidDTO>,ISaveSvc<PaidDTO> {

    fun getTotalPaid():BigDecimal
    fun getRecurrent(date: LocalDate):List<PaidDTO>
}