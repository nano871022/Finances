package co.com.japl.finances.iports.dtos

import java.math.BigDecimal


    data class CalcDTO (
        var name:String,
        var valueCredit:BigDecimal
        ,var interest:Double
        ,var period:Long
        ,var quoteCredit:BigDecimal
        ,var type:String
        ,var id:Int
        ,var interestValue:BigDecimal
        ,var capitalValue:BigDecimal
        ,var kindOfTax:String
    )
