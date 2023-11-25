package co.japl.android.finances.services.dto

import java.math.BigDecimal
import java.time.LocalDate

data class AdditionalCreditDTO (
    var id:Int,
    var name:String,
    var value:BigDecimal,
    var creditCode:Long,
    var startDate:LocalDate,
    var endDate:LocalDate
        )

object AdditionalCreditDB{
    object Entry{
        const val TABLE_NAME = "TB_ADDITIONAL_CREDIT"
        const val COLUMN_NAME = "adc_str_name"
        const val COLUMN_VALUE = "adc_num_value"
        const val COLUMN_CREDIT_CODE = "adc_cod_credit"
        const val COLUMN_START_DATE = "adc_dt_start"
        const val COLUMN_END_DATE = "adc_dt_end"
    }
}