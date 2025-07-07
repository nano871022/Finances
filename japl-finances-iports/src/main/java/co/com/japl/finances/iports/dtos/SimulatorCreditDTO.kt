package co.com.japl.finances.iports.dtos

import co.com.japl.finances.iports.enums.KindOfTaxEnum
import java.math.BigDecimal

data class SimulatorCreditDTO (
    var code:Int=0,
    var name:String?=null,
    var value:BigDecimal,
    var periods:Short,
    var tax:Double,
    var kindOfTax: KindOfTaxEnum,
    val isCreditVariable: Boolean = false,
    var interestValue:BigDecimal?=null,
    var capitalValue:BigDecimal?=null,
    var quoteValue: BigDecimal?=null
)