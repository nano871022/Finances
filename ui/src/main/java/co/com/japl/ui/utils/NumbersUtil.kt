package co.japl.android.myapplication.utils

import android.util.Log
import android.widget.EditText
import android.widget.TextView
import java.math.BigDecimal
import java.text.DecimalFormat

class NumbersUtil {

    companion object  {
        private  val formatDecimalMoneyCO = "$ #,###.00"
        private val formatDecimal = "#,###.00"

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

        fun toString(field:EditText): String{
            return if(field.text?.isNotBlank() == true) {
                 toString(field.text.toString().replace(",", "").toBigDecimal())
            }else{
                ""
            }
        }

        fun stringCOPToBigDecimal(value:String):BigDecimal{
            return if(value.isEmpty()){
                BigDecimal.ZERO
            }else {
                value.replace(" ", "").replace("$", "").replace("'", "").replace(",", "").trim().toBigDecimal()
            }
        }

        fun toBigDecimal(field:EditText):BigDecimal{
            return if(field.text?.isNotBlank() == true) {
                field.text.toString().replace("$", "").replace(",", "").trim().toBigDecimal()
            }else{
                BigDecimal.ZERO
            }
        }

        fun toBigDecimal(field:String):BigDecimal{
            return field?.takeIf { it.isNotBlank() }?.let { it.toString().replace("$", "").replace(",", "").trim().toBigDecimal() }?:BigDecimal.ZERO
        }
        fun toBigDecimal(field:TextView):BigDecimal{
            return if(field.text?.isNotBlank() == true) {
                field.text.toString().replace("$", "").replace(",", "").trim().toBigDecimal()
            }else{
                BigDecimal.ZERO
            }
        }

        fun toDouble(field:TextView):Double{
            return if(field.text?.isNotBlank() == true){
                field.text.toString().replace(",","").replace("%","").trim().toDouble()
            }else{
                0.0
            }

        }

        fun toDouble(value:String):Double{
            return if(value?.isNotBlank() == true){
                value.replace(",","").replace("%","").trim().toDouble()
            }else{
                0.0
            }
        }
        fun toLong(value:String):Long{
            return if(value?.isNotBlank() == true){
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
    }
}