package co.japl.finances.core.adapters.inbound.implement.credit

import co.com.japl.finances.iports.dtos.AmortizationRowDTO
import co.com.japl.finances.iports.dtos.SimulatorCreditDTO
import co.com.japl.finances.iports.enums.KindAmortization
import co.com.japl.finances.iports.inbounds.credit.ISimulatorCreditFixPort
import co.japl.finances.core.usercases.interfaces.common.ISimulatorCredit
import javax.inject.Inject

class SimulatorCreditFixImpl @Inject constructor(private val simulator: ISimulatorCredit) : ISimulatorCreditFixPort {
    override fun calculate(dto: SimulatorCreditDTO): SimulatorCreditDTO {
        require(dto.value.toDouble() > 0.0){"Value must be greater than 0"}
        require(dto.tax > 0.0){"Tax must be greater than 0"}
        require(dto.periods > 0){"Periods must be greater than 0"}
        require(dto.isCreditVariable.not()){"Credit variable must be false"}
        return simulator.calculate(dto)
    }

    override fun save(dto: SimulatorCreditDTO, cache: Boolean): Long {
        require(dto.value.toDouble() > 0.0){"Value must be greater than 0"}
        require(dto.tax > 0.0){"Tax must be greater than 0"}
        require(dto.periods > 0){"Periods must be greater than 0"}
        require(dto.quoteValue?.toDouble()?:0.0 > 0.0){"Quote Value must be greater than 0"}
        require(dto.capitalValue?.toDouble()?:0.0 > 0.0){"Capital Value must be greater than 0"}
        require(dto.interestValue?.toDouble()?:0.0 > 0.0){"Interest Value must be greater than 0"}
        require(dto.code == 0){"Code must be 0"}
        return simulator.save(dto, cache)
    }

    override fun update(dto: SimulatorCreditDTO, cache: Boolean): Boolean {
        require(dto.value.toDouble() > 0.0){"Value must be greater than 0"}
        require(dto.tax > 0.0){"Tax must be greater than 0"}
        require(dto.periods > 0){"Periods must be greater than 0"}
        require(dto.quoteValue?.toDouble()?:0.0 > 0.0){"Quote Value must be greater than 0"}
        require(dto.capitalValue?.toDouble()?:0.0 > 0.0){"Capital Value must be greater than 0"}
        require(dto.interestValue?.toDouble()?:0.0 > 0.0){"Interest Value must be greater than 0"}
        require(dto.code > 0){"Code must be greater than 0"}
        val resp =  simulator.save(dto, cache)
        return resp == dto.code.toLong()
    }

    override fun setSimulation(dto: SimulatorCreditDTO): Boolean {
        require(dto.value.toDouble() > 0.0){"Value must be greater than 0"}
        require(dto.tax > 0.0){"Tax must be greater than 0"}
        require(dto.periods > 0){"Periods must be greater than 0"}
        require(dto.quoteValue?.toDouble()?:0.0 >= 0.0){"Quote Value must be greater than 0"}
        require(dto.capitalValue?.toDouble()?:0.0 >= 0.0){"Capital Value must be greater than 0"}
        require(dto.interestValue?.toDouble()?:0.0 >= 0.0){"Interest Value must be greater than 0"}
        require(dto.code >= 0){"Code must be greater than 0"}
        val resp =  simulator.save(dto,true)
        return resp == dto.code.toLong()
    }
}
