package co.japl.android.finances.services.core.mapper

import co.com.japl.finances.iports.dtos.BoughtCreditCardPeriodDTO
import co.japl.android.finances.services.dto.CreditCardBoughtDTO
import co.japl.android.finances.services.dto.PeriodDTO
import com.google.common.collect.Multiset.Entry
import java.time.LocalDateTime

object BoughtCreditCardPeriodMapper {
    fun mapper(entryMap:Map.Entry<LocalDateTime, List<CreditCardBoughtDTO>>):Map.Entry<LocalDateTime, List<co.com.japl.finances.iports.dtos.CreditCardBoughtDTO>> {
        return hashMapOf(Pair(entryMap.key,entryMap.value.map { CreditCardBoughtMapper.mapper(it) })).entries.first()
    }
}