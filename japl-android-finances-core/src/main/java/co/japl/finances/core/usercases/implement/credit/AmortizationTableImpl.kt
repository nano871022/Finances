package co.japl.finances.core.usercases.implement.credit

import android.util.Log
import co.com.japl.finances.iports.dtos.AmortizationRowDTO
import co.com.japl.finances.iports.enums.KindAmortization
import co.com.japl.finances.iports.outbounds.ISimulatorCreditPort
import co.japl.finances.core.usercases.interfaces.common.ISimulatorCredit
import co.japl.finances.core.usercases.interfaces.credit.IAmortizationTable
import javax.inject.Inject

class AmortizationTableImpl @Inject constructor(private val simulatorSvc: ISimulatorCreditPort,private val simulatorCredit: ISimulatorCredit): IAmortizationTable {

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
        return listOf()
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