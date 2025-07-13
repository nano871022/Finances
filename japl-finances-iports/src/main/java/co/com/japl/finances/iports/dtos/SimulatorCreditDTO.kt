package co.com.japl.finances.iports.dtos

import co.com.japl.finances.iports.enums.KindOfTaxEnum
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class SimulatorCreditDTO (
    @SerializedName("id")
    var code:Int=0,
    var name:String?=null,
    @SerializedName("valueCredit")
    var value:BigDecimal,
    @SerializedName("period")
    var periods:Short,
    @SerializedName("interest")
    var tax:Double,
    var kindOfTax: KindOfTaxEnum,
    val isCreditVariable: Boolean = false,
    var interestValue:BigDecimal?=null,
    var capitalValue:BigDecimal?=null,
    @SerializedName("quoteCredit")
    var quoteValue: BigDecimal?=null
)