package co.com.japl.finances.iports.inbounds.credit

import co.com.japl.finances.iports.dtos.SimulatorCreditDTO
import java.util.Optional

interface ISimulatorCreditFixPort {

    fun get(codCredit:Int):Optional<SimulatorCreditDTO>

    fun get():List<SimulatorCreditDTO>

    fun delete(codCredit:Int):Boolean

    fun create(dto:SimulatorCreditDTO):Optional<Int>
}
