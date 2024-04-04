package co.japl.finances.core.utils

import java.time.LocalDateTime
import java.time.LocalTime

object SmsUtil {

    fun getValues(values:List<String>):Triple<String,Double,LocalDateTime>?{
        if(values.size >= 3) {
            val date = values.firstOrNull {
                it.isNotBlank() &&
                DateUtils.isLocalDateRegex(it)
            }?.let(DateUtils::toLocalDateRegex)
            val value = values.firstOrNull{
                it.isNotBlank() &&
                NumbersUtil.isNumberRegex(it)
            }?.let(NumbersUtil::toDoubleOrZero)
            val name = values.subList(1,values.size).firstOrNull{
                !DateUtils.isLocalDateRegex(it) &&
                !NumbersUtil.isNumberRegex(it) &&
                        it.isNotBlank()
            }
            if(name?.isNotBlank() == true && value != null && date != null) {
                return Triple(name, value, LocalDateTime.of(date,LocalTime.MAX))
            }
        }
        return null
    }
}