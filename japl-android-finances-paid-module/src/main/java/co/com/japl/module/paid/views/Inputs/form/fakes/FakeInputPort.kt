package co.com.japl.module.paid.views.Inputs.form.fakes

import co.com.japl.finances.iports.dtos.InputDTO
import co.com.japl.finances.iports.inbounds.inputs.IInputPort
import java.math.BigDecimal
import java.time.LocalDate

class FakeInputPort : IInputPort {
    override fun getById(id: Int): InputDTO? {
        return InputDTO(
            id = 1,
            date = LocalDate.now(),
            accountCode = 1,
            kindOf = "MONTHLY",
            name = "Test Input",
            value = BigDecimal.TEN,
            create = LocalDate.now(),
            end = LocalDate.MAX
        )
    }

    override fun get(codeAccount: Int): List<InputDTO> {
        return listOf(
            InputDTO(
                id = 1,
                date = LocalDate.now(),
                accountCode = 1,
                kindOf = "MONTHLY",
                name = "Test Input 1",
                value = BigDecimal.TEN,
                create = LocalDate.now(),
                end = LocalDate.MAX
            ),
            InputDTO(
                id = 2,
                date = LocalDate.now(),
                accountCode = 1,
                kindOf = "YEARLY",
                name = "Test Input 2",
                value = BigDecimal.ONE,
                create = LocalDate.now(),
                end = LocalDate.MAX
            )
        )
    }

    override fun create(dto: InputDTO): Boolean {
        return true
    }

    override fun update(dto: InputDTO): Boolean {
        return true
    }

    override fun delete(id: Int): Boolean {
        return true
    }

    override fun get(): List<InputDTO> {
        return get(1)
    }

    override fun save(dto: InputDTO): Boolean {
        return true
    }
}
