package co.japl.android.graphs.utils

import java.math.BigDecimal
import java.text.DecimalFormat

class NumbersUtil {

    companion object  {
        val formatDecimalMoneyCO = "$ #,###.00"
        val formatDecimal = "#,###.00"
        val format8Decimal = "#,###.00######"

        fun COPtoString(value: BigDecimal): String {
            return DecimalFormat(formatDecimalMoneyCO).format(value)
        }

        fun COPtoString(value: Double): String {
            return DecimalFormat(formatDecimalMoneyCO).format(value)
        }


        fun toString(value: Long): String {
            return DecimalFormat(formatDecimal).format(value)
        }

        fun toString(value: Double): String {
            return DecimalFormat(formatDecimal).format(value)
        }

        fun toString(value: BigDecimal): String {
            return DecimalFormat(formatDecimal).format(value)
        }

        fun stringCOPToBigDecimal(value:String):BigDecimal{
            return if(value.isEmpty()){
                BigDecimal.ZERO
            }else {
                value.replace(" ", "").replace("$", "").replace("'", "").replace(",", "").trim().toBigDecimal()
            }
        }

        fun toBigDecimal(field:String):BigDecimal{
            return field.takeIf { it.isNotBlank() }?.let { it.replace("$", "").replace(",", "").trim().toBigDecimal() }?:BigDecimal.ZERO
        }

        fun toDouble(value:String):Double{
            return if(value.isNotBlank()){
                value.replace(",","").replace("%","").trim().toDouble()
            }else{
                0.0
            }
        }
        fun toLong(value:String):Long{
            return if(value.isNotBlank()){
                value.replace(",","").replace("%","").trim().toLong()
            }else{
                0
            }
        }

        fun isNumber(value:String):Boolean{
            if(value.isEmpty()) return false
            try{
                toBigDecimal(value).toDouble()
                return true
            }catch(e:NumberFormatException){
                return false
            }
        }
        fun isIntNumber(value:String):Boolean{
            if(value.isEmpty()) return false
            try{
                value.toInt()
                return true
            }catch(e:NumberFormatException){
                return false
            }
        }
    }
}
