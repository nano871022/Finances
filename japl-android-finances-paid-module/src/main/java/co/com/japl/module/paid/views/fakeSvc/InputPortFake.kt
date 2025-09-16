package co.com.japl.module.paid.views.fakeSvc

import co.com.japl.finances.iports.dtos.InputDTO
import co.com.japl.finances.iports.inbounds.inputs.IInputPort
import java.time.YearMonth

class InputPortFake : IInputPort {
    override fun getInputs(accountCode: Int): List<InputDTO> {
        return emptyList()
    }

    override fun deleteRecord(id: Int): Boolean {
        return true
    }

    override fun updateValue(id: Int, value: Double): Boolean {
        return true
    }

    override fun getById(id: Int): InputDTO? {
        return null
    }

    override fun create(input: InputDTO): Boolean {
        return true
    }

    override fun update(input: InputDTO): Boolean {
        return true
    }

    override fun getTotalInputs(codeAccount: Int, period: YearMonth): Double {
        return 0.0
    }
}
