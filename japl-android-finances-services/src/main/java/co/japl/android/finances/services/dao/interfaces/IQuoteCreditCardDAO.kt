package co.japl.android.finances.services.dao.interfaces

import co.japl.android.finances.services.dto.CreditCardBoughtDTO
import co.japl.android.finances.services.dto.QuoteCreditCard
import co.japl.android.finances.services.interfaces.IGetPeriodsServices
import co.japl.android.finances.services.interfaces.SaveSvc
import co.japl.android.finances.services.interfaces.SearchSvc
import java.time.LocalDateTime
import java.time.YearMonth

interface IQuoteCreditCardDAO:  SaveSvc<CreditCardBoughtDTO>,
    SearchSvc<CreditCardBoughtDTO>, IGetPeriodsServices {
        public fun getRecurrentPendingQuotes(key: Int, cutOff: LocalDateTime):List<CreditCardBoughtDTO>

        fun endingRecurrentPayment(idBought: Int,cutOff:LocalDateTime):Boolean

        fun getLastAvailableQuotesTC():List<QuoteCreditCard>

        fun getPeriod(id:Int):List<YearMonth>

}