package co.com.japl.ui.utils

import android.icu.text.DecimalFormat
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import co.japl.android.myapplication.utils.NumbersUtil

class CurrencyVisualTransformation (private val formatDecimal:String = NumbersUtil.formatDecimal, private val currencyForce:Boolean = false,private val decimalForce:Boolean = false):VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        if(text.text.isBlank() || (currencyForce && !NumbersUtil.isNumber(text.text))){
            return TransformedText(text, OffsetMapping.Identity)
        }
        if(NumbersUtil.isNumber(text.text).not() && currencyForce.not() && decimalForce.not()){
            return TransformedText(text, OffsetMapping.Identity)
        }
        if(NumbersUtil.isIntNumber(text.text) && currencyForce.not() && decimalForce){
            return TransformedText(text, OffsetMapping.Identity)
        }
        val transformation = DecimalFormat(formatDecimal).format(NumbersUtil.toDouble(text.text))

        return TransformedText(AnnotatedString(transformation), CurrencyOffSetMapping(text.text,transformation,'.',','))
    }

}