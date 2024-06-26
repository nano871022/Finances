package co.japl.android.myapplication.bussiness.DTO

import java.time.LocalDateTime

data class TaxDTO (
    var id:Int,
    var month:Short,
    var year:Int,
    var status:Short,
    var codCreditCard:Int,
    var create: LocalDateTime,
    var value: Double,
    var kind: Short,
    var period: Short,
    var kindOfTax: String?
    )

object TaxDB{
    object TaxEntry{
        const val TABLE_NAME = "TB_TAX_CREDIT_CARD"
        const val COLUMN_MONTH = "num_month"
        const val COLUMN_YEAR = "num_year"
        const val COLUMN_status = "num_status"
        const val COLUMN_COD_CREDIT_CARD = "cod_credit_card"
        const val COLUMN_CREATE_DATE = "dt_create"
        const val COLUMN_TAX = "num_tax"
        const val COLUMN_KIND = "num_kind"
        const val COLUMN_PERIOD = "num_period"
        const val COLUMN_KIND_OF_TAX = "str_kind_of_tax"
    }
}