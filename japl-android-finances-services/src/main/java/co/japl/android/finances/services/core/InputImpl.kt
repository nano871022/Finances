package co.japl.android.finances.services.core

import co.com.japl.finances.iports.dtos.InputDTO
import co.japl.android.finances.services.interfaces.IInputSvc
import co.com.japl.finances.iports.outbounds.IInputPort
import co.japl.android.finances.services.core.mapper.InputMapper
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject
import kotlin.jvm.optionals.getOrNull

class InputImpl @Inject constructor(private val inputImpl:IInputSvc) : IInputPort {
    override fun getTotalInputs(): BigDecimal? {
        return inputImpl.getTotalInputs() + inputImpl.getTotalInputsSemestral()
    }

    override fun getInputs(): List<InputDTO> {
        return inputImpl.getAllValid(LocalDate.now().withDayOfMonth(1).minusDays(1)).map ( InputMapper::mapper )
    }

    override fun deleteRecord(id: Int): Boolean {
        return inputImpl.delete(id)
    }

    override fun create(input: InputDTO): Int {
        require(input.id == 0)
        return inputImpl.save(InputMapper.mapper(input)).toInt()
    }

    override fun update(input:InputDTO): Boolean {
        require(input.id > 0)
        return inputImpl.save(InputMapper.mapper(input)) > 0
    }

    override fun getInput(id: Int): InputDTO? {
        return inputImpl.get(id).takeIf { it.isPresent }?.map (InputMapper::mapper)?.getOrNull()
    }
}