package co.japl.android.finances.services.interfaces

import co.japl.android.finances.services.dto.CreditCardBoughtDTO
import co.japl.android.finances.services.dto.QuoteCreditCard
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.YearMonth

interface IQuoteCreditCardSvc:  SaveSvc<CreditCardBoughtDTO>,
    SearchSvc<CreditCardBoughtDTO>, IGetPeriodsServices  {
        public fun getRecurrentPendingQuotes(key: Int, cutOff: LocalDateTime):List<CreditCardBoughtDTO>

        fun endingRecurrentPayment(idBought: Int,cutOff:LocalDateTime):Boolean

        fun getLastAvailableQuotesTC():List<QuoteCreditCard>

        fun getPeriod(id:Int):List<YearMonth>

}