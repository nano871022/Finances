package co.japl.android.myapplication.finanzas.bussiness.interfaces

import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.DifferInstallmentDTO
import java.time.LocalDate

interface IDifferInstallment : SaveSvc<DifferInstallmentDTO> {

    fun get(cutOff: LocalDate):List<DifferInstallmentDTO>
}