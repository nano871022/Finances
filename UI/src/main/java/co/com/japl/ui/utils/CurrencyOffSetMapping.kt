package co.com.japl.ui.utils

import androidx.compose.ui.text.input.OffsetMapping

class CurrencyOffSetMapping constructor(private val originalText:String, private val formattedText:String): OffsetMapping {
    private val indexes = findDigitIndexes(originalText,formattedText)
    override fun originalToTransformed(offset: Int): Int {
        if(offset >= originalText.length){
            return indexes.last() + 1
        }
        return indexes[offset]
    }

    override fun transformedToOriginal(offset: Int): Int {
        return indexes.indexOfFirst { it > offset }.takeIf { it != -1 } ?: originalText.length
    }

    private fun findDigitIndexes(firstString:String,secondString:String):List<Int>{
        val digitIndexes = mutableListOf<Int>()
        var currentIndex = 0
        for(digit in firstString){
            val index = secondString.indexOf(digit,currentIndex)
            if(index != -1){
                digitIndexes.add(index)
                currentIndex = index + 1
            }else{
                return emptyList()
            }
        }
        return digitIndexes
    }
}