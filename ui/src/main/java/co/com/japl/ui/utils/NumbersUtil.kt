package co.japl.android.myapplication.utils

import android.util.Log
import android.widget.EditText
import android.widget.TextView
import java.math.BigDecimal
import java.text.DecimalFormat

class NumbersUtil {

    companion object  {
        val formatDecimalMoneyCO = "$ #,###.00"
        val formatDecimal = "#,###.00"
        val format8Decimal = "#,###.00######"

        val format4Decimal = "#,###.00##"

        val formatLong = "#,###"

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

        fun toStringLong(value: Long): String {
            return DecimalFormat(formatLong).format(value)
        }

        fun toString4(value: Double): String {
            return DecimalFormat(format4Decimal).format(value)
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

        fun toDouble(value:String):Double{
            return if(value?.trim()?.isNotBlank() == true){
                try {
                    value.replace(",","").replace("%","").trim().toDouble()
                }catch(e:Exception){
                    0.0
                }
            }else{
                0.0
            }
        }
        fun toLong(value:String):Long{
            return if(value?.trim()?.isNotBlank() == true){
                try {
                    value.replace(",","").replace("%","").trim().toLong()
                }catch(e:Exception){
                    0
                }
            }else{
                0
            }
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
        fun isIntNumber(value:String):Boolean{
            if(value.trim().isEmpty()) return false
            return try{
                value.trim().toInt()
                true
            }catch(e:Exception){
                false
            }
        }

        fun bytesConvert(bytes:Double): String{
            if(bytes > 1024) {
                val kb = bytes / 1024
                if (kb > 1024) {
                    val mb = kb / 1024
                    if (mb > 1024) {
                        return "${toString(mb / 1024)} Gb"
                    }
                    return "${toString(mb)} Mb"
                }
                return "${toString(kb)} Kb"
            }
            return "${toString(bytes)} B"
        }

        fun bytesConvert(bytes:Long): String{
            if(bytes > 1024) {
                val kb = bytes / 1024
                if (kb > 1024) {
                    val mb = kb / 1024
                    if (mb > 1024) {
                        return "${toString(mb / 1024)} Gb"
                    }
                    return "${toString(mb)} Mb"
                }
                return "${toString(kb)} Kb"
            }
            return "${toString(bytes)} B"
        }
    }
}