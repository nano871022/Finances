package co.japl.finances.core.utils

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

        fun toDoubleOrZero(value:String):Double{
            val postComma = value.indexOf(",")
            val postDot = value.indexOf(".")
            if(postDot >= 0 && postComma >=0 && postComma > postDot){
                return value.replace(".","").replace(",",".").toDouble()
            }else if(postComma == -1 && postDot > 0 && postDot < value.length - 3){
                return value.replace(".","").toDouble()
            }else if(postDot == -1 && postComma > 0 && postComma < value.length - 3){
                return value.replace(",","").toDouble()
            }else{
                return value.replace(",","").toDouble()
            }
        }

        fun isNumberRegex(value:String):Boolean{
            return "[\\.\\,\\d]+".toRegex().matches(value)
        }

        fun isNumber(value:String):Boolean{
            val cleanValue = value.trim().replace("$", "").replace(",", "")
            if(cleanValue.isEmpty()) return false
            return try{
                cleanValue.toBigDecimal()
                true
            }catch(e:Exception){
                false
            }
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
                try {
                 toString(field.text.toString().replace(",", "").toBigDecimal())
                }catch(e:Exception){
                    ""
                }
            }else{
                ""
            }
        }

        fun stringCOPToBigDecimal(value:String):BigDecimal{
            return if(value.trim().isEmpty()){
                BigDecimal.ZERO
            }else {
                try {
                    value.replace(" ", "").replace("$", "").replace("'", "").replace(",", "").trim().toBigDecimal()
                }catch(e:Exception){
                    BigDecimal.ZERO
                }
            }
        }

        fun toBigDecimal(field:EditText):BigDecimal{
            return if(field.text?.isNotBlank() == true) {
                try {
                    field.text.toString().replace("$", "").replace(",", "").trim().toBigDecimal()
                }catch(e:Exception){
                    BigDecimal.ZERO
                }
            }else{
                BigDecimal.ZERO
            }
        }

        fun toBigDecimal(field:String):BigDecimal{
            return field?.takeIf { it.trim().isNotBlank() }?.let {
                try {
                    it.toString().replace("$", "").replace(",", "").trim().toBigDecimal()
                }catch(e:Exception){
                    BigDecimal.ZERO
                }
            }?:BigDecimal.ZERO
        }
        fun toBigDecimal(field:TextView):BigDecimal{
            return if(field.text?.isNotBlank() == true) {
                try {
                    field.text.toString().replace("$", "").replace(",", "").trim().toBigDecimal()
                }catch(e:Exception){
                    BigDecimal.ZERO
                }
            }else{
                BigDecimal.ZERO
            }
        }

        fun toDouble(field:TextView):Double{
            return if(field.text?.trim()?.isNotBlank() == true){
                try {
                    field.text.toString().replace(",","").replace("%","").trim().toDouble()
                }catch(e:Exception){
                    0.0
                }
            }else{
                0.0
            }

        }
    }
}