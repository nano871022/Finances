package co.japl.finances.core.adapters.inbound.implement.creditCard

import co.com.japl.finances.iports.dtos.SimulatorCreditDTO
import co.com.japl.finances.iports.inbounds.creditcard.ISimulatorCreditVariablePort
import co.japl.finances.core.usercases.interfaces.common.ISimulatorCredit
import java.math.BigDecimal
import javax.inject.Inject

class SimulatorImpl @Inject constructor(private val simulatorSvc: ISimulatorCredit): ISimulatorCreditVariablePort {

    override fun calculate(dto: SimulatorCreditDTO): SimulatorCreditDTO {
        require(dto.isCreditVariable){"El tipo de credito debe ser variable"}
        require(dto.value > BigDecimal.ZERO){"El valor debe ser mayor a 0"}
        require(dto.periods > 0){"El numero de periodos debe ser mayor a 0"}
        require(dto.tax > 0){"El valor del impuesto debe ser mayor a 0"}

        return simulatorSvc.calculate(dto)
    }

    override fun save(dto: SimulatorCreditDTO,cache:Boolean): Long {
        require(dto.code == 0){"El codigo debe ser 0 para poder guardar"}
        require(dto.name != null){"El nombre no puede ser nulo"}
        require(dto.value > BigDecimal.ZERO){"El valor debe ser mayor a 0"}
        require(dto.periods > 0){"El numero de periodos debe ser mayor a 0"}
        require(dto.tax > 0){"El valor del impuesto debe ser mayor a 0"}
        require(dto.isCreditVariable){"El tipo de credito debe ser variable"}
        require((dto.interestValue ?: BigDecimal.ZERO) > BigDecimal.ZERO){"El valor de interes no puede ser nulo"}
        require((dto.capitalValue?:BigDecimal.ZERO) > BigDecimal.ZERO){"El valor de capital no puede ser nulo"}
        require((dto.quoteValue?:BigDecimal.ZERO) > BigDecimal.ZERO){"El valor de cuota no puede ser nulo"}

        return  simulatorSvc.save(dto,cache)
    }

    override fun update(
        dto: SimulatorCreditDTO,
        cache: Boolean
    ): Boolean {
        require(dto.code > 0){"El codigo debe ser mayor 0 para poder guardar"}
        require(dto.name != null){"El nombre no puede ser nulo"}
        require(dto.value > BigDecimal.ZERO){"El valor debe ser mayor a 0"}
        require(dto.periods > 0){"El numero de periodos debe ser mayor a 0"}
        require(dto.tax > 0){"El valor del impuesto debe ser mayor a 0"}
        require(dto.isCreditVariable){"El tipo de credito debe ser variable"}
        require((dto.interestValue ?: BigDecimal.ZERO) > BigDecimal.ZERO){"El valor de interes no puede ser nulo"}
        require((dto.capitalValue?:BigDecimal.ZERO) > BigDecimal.ZERO){"El valor de capital no puede ser nulo"}
        require((dto.quoteValue?:BigDecimal.ZERO) > BigDecimal.ZERO){"El valor de cuota no puede ser nulo"}

        val resp = simulatorSvc.save(dto,cache)
        return resp == dto.code.toLong()
    }

    override fun setSimulation(dto: SimulatorCreditDTO): Boolean {
        require(dto.code > 0){"El codigo debe ser mayor 0 para poder guardar"}
        require(dto.name != null){"El nombre no puede ser nulo"}
        require(dto.value > BigDecimal.ZERO){"El valor debe ser mayor a 0"}
        require(dto.periods > 0){"El numero de periodos debe ser mayor a 0"}
        require(dto.tax >= 0){"El valor del impuesto debe ser mayor a 0"}
        require(dto.isCreditVariable){"El tipo de credito debe ser variable"}
        require((dto.interestValue ?: BigDecimal.ZERO) >= BigDecimal.ZERO){"El valor de interes no puede ser nulo"}
        require((dto.capitalValue?:BigDecimal.ZERO) >= BigDecimal.ZERO){"El valor de capital no puede ser nulo"}
        require((dto.quoteValue?:BigDecimal.ZERO) >= BigDecimal.ZERO){"El valor de cuota no puede ser nulo"}

        val resp = simulatorSvc.save(dto,true)
        return resp == dto.code.toLong()
    }


}