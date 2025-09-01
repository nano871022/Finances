package co.com.japl.finances.iports.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.math.BigDecimal
import java.lang.reflect.Type

class BigDecimalDeserializer : JsonDeserializer<BigDecimal> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): BigDecimal? {
        return try {
            BigDecimal(json?.asString)
        } catch (e: NumberFormatException) {
            null // O maneja el error de otra manera
        }
    }
}