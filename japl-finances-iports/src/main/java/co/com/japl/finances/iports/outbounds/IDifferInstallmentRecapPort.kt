package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.dtos.DifferInstallmentDTO
import java.time.LocalDate

interface IDifferInstallmentRecapPort {

    fun get(cutOff: LocalDate):List<DifferInstallmentDTO>

    fun get(id:Int):DifferInstallmentDTO?

}