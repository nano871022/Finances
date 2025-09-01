package co.com.japl.finances.iports.inbounds.creditcard

import co.com.japl.finances.iports.dtos.SimulatorCreditDTO

interface ISimulatorCreditVariablePort {

    fun calculate(dto: SimulatorCreditDTO): SimulatorCreditDTO

    fun save(dto: SimulatorCreditDTO,cache:Boolean=false):Long

    fun update(dto:SimulatorCreditDTO,cache:Boolean):Boolean

    fun setSimulation(dto:SimulatorCreditDTO):Boolean

    fun getList():List<SimulatorCreditDTO>
}