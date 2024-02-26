package co.com.japl.ui.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.core.text.isDigitsOnly
import co.japl.android.myapplication.utils.NumbersUtil

class CurrencyVisualTransformation (currencyCode:String):VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originValue = text.text.trim()
        if(originValue.isEmpty()){
            return TransformedText(text, OffsetMapping.Identity)
        }
        if(originValue.isDigitsOnly().not()){
            return TransformedText(text, OffsetMapping.Identity)
        }
        val formattedText = NumbersUtil.toString(originValue.toBigDecimal())
        return TransformedText( AnnotatedString(formattedText),CurrencyOffSetMapping(originValue,formattedText))
    }

}