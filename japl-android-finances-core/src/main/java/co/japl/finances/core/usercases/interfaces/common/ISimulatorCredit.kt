package co.japl.finances.core.usercases.interfaces.common

import co.com.japl.finances.iports.dtos.SimulatorCreditDTO

interface ISimulatorCredit {

    fun calculate(dto: SimulatorCreditDTO): SimulatorCreditDTO

    fun save(dto: SimulatorCreditDTO):Boolean

}