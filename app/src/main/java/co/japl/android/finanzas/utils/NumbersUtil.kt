package co.japl.android.finanzas.utils

import java.math.BigDecimal
import java.text.DecimalFormat

class NumbersUtil {

    companion object  {
        private  val formatDecimalMoneyCO = "$ #,###.##"
        private val formatDecimal = "#,###.##"

        fun COPtoString(value: BigDecimal): String {
            return DecimalFormat(formatDecimalMoneyCO).format(value)
        }

        fun toString(value: Long): String {
            return DecimalFormat(formatDecimal).format(value)
        }
    }
}