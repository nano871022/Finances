package co.com.japl.finances.iports.dtos

import android.provider.BaseColumns
import co.com.japl.finances.iports.enums.KindOfTaxEnum
import co.com.japl.finances.iports.enums.TaxEnum
import java.math.BigDecimal
import java.time.LocalDateTime

data class CreditCardBoughtItemDTO (
    var codeCreditCard: Int,
    var nameCreditCard: String,
    var nameItem: String,
    var valueItem: Double,
    var interest:Double,
    var month: Int,
    var boughtDate: LocalDateTime,
    var cutOutDate: LocalDateTime,
    var createDate: LocalDateTime,
    var endDate: LocalDateTime,
    var id:Int,
    var recurrent:Boolean,
    var kind:TaxEnum,
    var kindOfTax:KindOfTaxEnum,

    var monthPaid:Long,
    var capitalValue:Double,
    var interestValue:Double,
    var settings:Double,
    var settingCode:Int,
    var pendingToPay:Double,
    var quoteValue:Double,
    var tagName:String

)