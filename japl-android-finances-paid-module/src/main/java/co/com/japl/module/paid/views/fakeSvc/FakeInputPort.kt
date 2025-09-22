package co.com.japl.module.paid.views.fakeSvc

import co.com.japl.finances.iports.dtos.InputDTO
import co.com.japl.finances.iports.inbounds.inputs.IInputPort
import java.time.YearMonth

class FakeInputPort : IInputPort {
    override fun getInputs(accountCode: Int): List<InputDTO> {
        TODO("Not yet implemented")
    }

    override fun deleteRecord(id: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun updateValue(id: Int, value: Double): Boolean {
        TODO("Not yet implemented")
    }

    override fun getById(id: Int): InputDTO? {
        TODO("Not yet implemented")
    }

    override fun create(input: InputDTO): Boolean {
        TODO("Not yet implemented")
    }

    override fun update(input: InputDTO): Boolean {
        TODO("Not yet implemented")
    }

    override fun getTotalInputs(
        codeAccount: Int,
        period: YearMonth
    ): Double {
        TODO("Not yet implemented")
    }


}