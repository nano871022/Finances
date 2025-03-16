package co.com.japl.finances.iports.utils

import android.icu.text.DecimalFormat
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.math.BigDecimal
import java.lang.reflect.Type

class BigDecimalSerializer : JsonSerializer<BigDecimal> {
    override fun serialize(
        src: BigDecimal?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(DecimalFormat("0.####").format(src))
    }

}