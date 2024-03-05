package co.japl.android.finances.services.core

import co.com.japl.finances.iports.dtos.InputDTO
import co.japl.android.finances.services.dao.interfaces.IInputSvc
import co.com.japl.finances.iports.outbounds.IInputPort
import co.japl.android.finances.services.core.mapper.InputMapper
import co.japl.finances.core.utils.DateUtils
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject
import kotlin.jvm.optionals.getOrNull

class InputImpl @Inject constructor(private val inputImpl:IInputSvc) : IInputPort {
    override fun getTotalInputs(cutOff:LocalDate): BigDecimal? {
        val records = inputImpl.getAll().filter{ it.dateEnd.isAfter(cutOff) }
        val monthly = records.filter {
            it.kindOf == "Mensual"
        }.sumOf { it.value }

        val semestral = records.filter {
            it.kindOf == "semestral" &&
            it.date.plusMonths(12).monthValue == cutOff.monthValue
        }.sumOf { it.value }

        val annual = records.filter {
            it.kindOf == "Anual" &&
            it.date.plusYears(1).year == cutOff.year
        }.sumOf { it.value }

        val biMonthly = records.filter {
            it.kindOf == "Bi-Mensual" &&
            it.date.plusMonths(2).monthValue == cutOff.monthValue
        }.sumOf { it.value }


        val trimestral = records.filter {
            it.kindOf == "Trimestral" &&
            it.date.plusMonths(3).monthValue == cutOff.monthValue
        }.sumOf { it.value }

        val cuatrimestral = records.filter {
            it.kindOf == "Cuatrimestral" &&
                    it.date.plusMonths(4).monthValue == cutOff.monthValue
        }.sumOf { it.value }

        return monthly + semestral + annual + biMonthly + trimestral + cuatrimestral
    }

    override fun getInputs(accountCode:Int): List<InputDTO> {
        return inputImpl.getAllValid(accountCode,LocalDate.now().withDayOfMonth(1).minusDays(1)).map ( InputMapper::mapper )
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