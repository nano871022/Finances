package co.com.japl.finances.iports.inbounds.credit

import co.com.japl.finances.iports.dtos.SimulatorCreditDTO

import co.com.japl.finances.iports.dtos.AmortizationRowDTO
import co.com.japl.finances.iports.enums.KindAmortization

interface ISimulatorCreditFixPort {

    fun calculate(dto: SimulatorCreditDTO): SimulatorCreditDTO

    fun save(dto: SimulatorCreditDTO,cache:Boolean=false):Long

    fun update(dto:SimulatorCreditDTO,cache:Boolean):Boolean

    fun setSimulation(dto:SimulatorCreditDTO):Boolean

    fun getAmortization(id: Int, kind: KindAmortization, cache: Boolean): List<AmortizationRowDTO>
}
