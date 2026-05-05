package co.japl.android.finances.services.utils

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