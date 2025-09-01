package co.com.japl.ui.utils

import android.util.Log
import androidx.compose.ui.text.input.OffsetMapping

class CurrencyOffSetMapping constructor(private val originalText:String,
                                        private val formattedText:String,
                                        private val decimalSeparator: Char = '.',
                                        private val groupingSeparator: Char = ','): OffsetMapping {


    override fun originalToTransformed(offset: Int): Int {
        var offsetEnd = offset
        val charO = if(originalText.length > offset) {
            originalText[offset]
        }else{
            ""
        }
        val charF = if(formattedText.length > offset) {
            formattedText[offset]
        }else{
            ""
        }
        if(charO != charF && formattedText.length > offset){
            val count = formattedText.substring(0,offset).count { it == groupingSeparator || it == decimalSeparator }
            offsetEnd = offset + count
        }
        if(formattedText.length <= offsetEnd){
            offsetEnd = formattedText.length
        }
        return offsetEnd
    }

    override fun transformedToOriginal(offset: Int): Int {
        var offsetEnd = offset
        val char = if(originalText.length > offset) {
            originalText[offset]
        }else{
            ""
        }
        val chart = if(formattedText.length > offset) {
            formattedText[offset]
        }else{
            ""
        }
        if(char != chart){
            val count = formattedText.substring(0,offset).count { it == groupingSeparator || it == decimalSeparator }
            offsetEnd = offset + count
        }
        if(originalText.length <= offsetEnd){
            offsetEnd = originalText.length
        }
        return offsetEnd
    }




}