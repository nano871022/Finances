package co.japl.finances.core.usercases.interfaces.common

import co.com.japl.finances.iports.dtos.SimulatorCreditDTO
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import java.math.BigDecimal

interface ISimulatorCredit {

    fun calculate(dto: SimulatorCreditDTO): SimulatorCreditDTO

    fun save(dto: SimulatorCreditDTO,cache:Boolean):Long

    fun getAll(isVariable:Boolean):List<SimulatorCreditDTO>

    fun delete(code:Int):Boolean

    fun calculateQuote(valueCredit: BigDecimal,creditRate:Double,kindOfRate: KindOfTaxEnum,monthPaid:Int,months:Int,quoteNum:Int): Triple<BigDecimal,BigDecimal,BigDecimal>

    fun calculateFixQuote(valueCredit: BigDecimal,creditRate:Double,kindOfRate: KindOfTaxEnum,monthPaid:Int,months:Int,quoteNum:Int): Triple<BigDecimal,BigDecimal,Pair<BigDecimal,BigDecimal>>
}