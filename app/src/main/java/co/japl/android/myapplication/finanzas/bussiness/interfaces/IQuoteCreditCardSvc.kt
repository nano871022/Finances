package co.japl.android.myapplication.finanzas.bussiness.interfaces

import co.japl.android.myapplication.bussiness.DTO.CreditCardBoughtDTO
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.bussiness.interfaces.SearchSvc
import java.math.BigDecimal
import java.time.LocalDateTime

interface IQuoteCreditCardSvc:  SaveSvc<CreditCardBoughtDTO>,
    SearchSvc<CreditCardBoughtDTO>, IGetPeriodsServices  {
        public fun getTotalQuoteTC():BigDecimal
        public fun getRecurrentPendingQuotes(key: Int, cutOff: LocalDateTime):List<CreditCardBoughtDTO>
}