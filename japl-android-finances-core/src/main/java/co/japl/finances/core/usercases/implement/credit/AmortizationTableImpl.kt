package co.japl.finances.core.usercases.implement.credit

import android.util.Log
import co.com.japl.finances.iports.dtos.AmortizationRowDTO
import co.com.japl.finances.iports.enums.KindAmortization
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.outbounds.ICreditPort
import co.com.japl.finances.iports.outbounds.ISimulatorCreditPort
import co.japl.finances.core.usercases.interfaces.common.ISimulatorCredit
import co.japl.finances.core.usercases.interfaces.credit.IAmortizationTable
import java.math.BigDecimal
import javax.inject.Inject

class AmortizationTableImpl @Inject constructor(
    private val simulatorSvc: ISimulatorCreditPort,
    private val simulatorCredit: ISimulatorCredit,
    private val creditSvc: ICreditPort
): IAmortizationTable {

    override fun getAmortization(
        code: Int,
        kind: KindAmortization,
        cache: Boolean
    ) :List<AmortizationRowDTO>{
        return when(kind){
            KindAmortization.FIXED_QUOTE -> getFixedQuote(code,cache)
            KindAmortization.FIXED_QUOTE_SIMULATOR -> getFixedQuoteSimulator(code,cache)
            KindAmortization.VARIABLE_QUOTE -> getVariableQuote(code,cache)
            KindAmortization.VARIABLE_QUOTE_SIMULATOR -> getVariableQuoteSimulator(code,cache)
        }
    }

    private fun getFixedQuote(code:Int,cache:Boolean):List<AmortizationRowDTO>{
        val credit = creditSvc.getById(code)
        return Array(credit?.periods?:0){
            val kindOfTax = credit?.kindOfTax ?: KindOfTaxEnum.ANUAL_EFFECTIVE
            val creditValue = credit?.value?: BigDecimal.ZERO
            val (interest,capital,quoteAndPending)  = simulatorCredit.calculateFixQuote(
                creditValue,
                credit?.tax?:0.0,
                kindOfTax,
                it,
                credit?.periods?:0,
                it)
            val (quote, pending) = quoteAndPending
            AmortizationRowDTO(
                (it + 1).toShort(),
                credit?.periods?.toShort()?:0.toShort(),
                credit?.tax?:0.0,
                kindOfTax,
                creditValue,
                pending,
                capital,
                interest,
                quote
            )
        }.toList()
    }
    private fun getVariableQuote(code:Int,cache:Boolean):List<AmortizationRowDTO>{
        return listOf()
    }
    private fun getVariableQuoteSimulator(code:Int,cache:Boolean):List<AmortizationRowDTO>{
        simulatorSvc.findByCode(code,cache)?.let{ dto ->
            Log.d(javaClass.simpleName,"<<<=== GetVariableQuoteSimulator:: $code $dto")
            return Array(dto.periods.toInt()){
                val calculate = simulatorCredit.calculateQuote(dto.value,dto.tax,dto.kindOfTax,it,dto.periods.toInt(),it)
                AmortizationRowDTO(
                    (it + 1).toShort(),
                    dto.periods,
                    dto.tax,
                    dto.kindOfTax,
                    dto.value,
                    dto.value - (calculate.second * it.toBigDecimal()),
                    calculate.second,
                    calculate.first,
                    calculate.third
                )
            }.toList()
        }
        return listOf()
    }
    private fun getFixedQuoteSimulator(code:Int,cache:Boolean):List<AmortizationRowDTO>{
        simulatorSvc.findByCode(code,cache)?.let{ dto ->
            Log.d(javaClass.simpleName,"<<<=== GetFixQuoteSimulator:: $code $dto")
            return Array(dto.periods.toInt()){
                val (interest,capital,quoteAndPending) = simulatorCredit.calculateFixQuote(
                    valueCredit = dto.value,
                    creditRate = dto.tax,
                    kindOfRate = dto.kindOfTax,
                    monthPaid = it  ,
                    months = dto.periods.toInt(),
                    quoteNum = it)
                val (quote, pending) = quoteAndPending
                AmortizationRowDTO(
                    (it + 1).toShort(),
                    dto.periods,
                    dto.tax,
                    dto.kindOfTax,
                    dto.value,
                    pending,
                    capital,
                    interest,
                    quote
                )
            }.toList()
        }
        return listOf()
    }
}