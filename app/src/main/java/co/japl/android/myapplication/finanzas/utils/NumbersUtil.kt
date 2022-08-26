package co.japl.android.myapplication.utils

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

        fun stringCOPToBigDecimal(value:String):BigDecimal{
            return value.replace("$","").replace(",","").trim().toBigDecimal()
        }
    }
}