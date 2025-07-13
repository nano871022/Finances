package co.japl.finances.core.inbounds.implement.credit

import co.com.japl.finances.iports.dtos.AmortizationRowDTO
import co.com.japl.finances.iports.dtos.SimulatorCreditDTO
import co.com.japl.finances.iports.enums.KindAmortization
import co.com.japl.finances.iports.inbounds.credit.ISimulatorCreditFixPort
import co.japl.finances.core.usercases.interfaces.common.ISimulatorCredit
import javax.inject.Inject

class SimulatorCreditFixImpl @Inject constructor(private val simulator: ISimulatorCredit) : ISimulatorCreditFixPort {
    override fun calculate(dto: SimulatorCreditDTO): SimulatorCreditDTO {
        return simulator.calculate(dto)
    }

    override fun save(dto: SimulatorCreditDTO, cache: Boolean): Long {
        return simulator.save(dto, cache)
    }

    override fun update(dto: SimulatorCreditDTO, cache: Boolean): Boolean {
        return simulator.update(dto, cache)
    }

    override fun setSimulation(dto: SimulatorCreditDTO): Boolean {
        return simulator.setSimulation(dto)
    }

    override fun getAmortization(id: Int, kind: KindAmortization, cache: Boolean): List<AmortizationRowDTO> {
        return simulator.getAmortization(id, kind, cache)
    }
}
