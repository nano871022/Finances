package co.japl.android.myapplication.finanzas.bussiness.interfaces

import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidDTO
import java.math.BigDecimal

interface IPaidSvc: SaveSvc<PaidDTO>,ISaveSvc<PaidDTO> {

    fun getTotalPaid():BigDecimal
}