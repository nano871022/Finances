package co.japl.finances.core.utils

import java.time.LocalDateTime
import java.time.LocalTime

object ExtractItemPatternUtil {

    fun getValues(groupValuesExpReg:List<String>,defaultDate:LocalDateTime):Triple<String,Double,LocalDateTime>?{
            val date = groupValuesExpReg.firstOrNull {
                it.isNotBlank() &&
                DateUtils.isLocalDateRegex(it)
            }?.let{
                val dtStr = DateUtils.toLocalDateRegex(it)
                LocalDateTime.of(dtStr,LocalTime.MAX)
            }

            val value = groupValuesExpReg.firstOrNull{
                it.isNotBlank() &&
                NumbersUtil.isNumberRegex(it)
            }?.let{NumbersUtil.toDoubleOrZero(it)}

            val name = groupValuesExpReg.subList(1,groupValuesExpReg.size).firstOrNull{
                it.isNotBlank() &&
                !NumbersUtil.isNumberRegex(it) &&
                !DateUtils.isLocalDateRegex(it)
            }

            if(name?.isNotBlank() == true && value != null) {
                return Triple(name, value, date?:defaultDate)
            }
        return null
    }
}