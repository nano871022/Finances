package co.com.japl.module.credit.views.fakesSvc

import co.com.japl.finances.iports.dtos.SimulatorCreditDTO
import co.com.japl.finances.iports.inbounds.credit.ISimulatorCreditFixPort

class SimulatorCreditFixFake : ISimulatorCreditFixPort{
    override fun calculate(dto: SimulatorCreditDTO): SimulatorCreditDTO {
        TODO("Not yet implemented")
    }

    override fun save(
        dto: SimulatorCreditDTO,
        cache: Boolean
    ): Long {
        TODO("Not yet implemented")
    }

    override fun update(
        dto: SimulatorCreditDTO,
        cache: Boolean
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun setSimulation(dto: SimulatorCreditDTO): Boolean {
        TODO("Not yet implemented")
    }

    override fun getList(): List<SimulatorCreditDTO> {
        TODO("Not yet implemented")
    }
}