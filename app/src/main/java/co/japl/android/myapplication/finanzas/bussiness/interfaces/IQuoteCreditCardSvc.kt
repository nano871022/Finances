package co.japl.android.myapplication.finanzas.bussiness.interfaces

import co.japl.android.myapplication.bussiness.DTO.CreditCardBoughtDTO
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.bussiness.interfaces.SearchSvc
import co.japl.android.myapplication.finanzas.pojo.QuoteCreditCard
import java.math.BigDecimal
import java.time.LocalDateTime

interface IQuoteCreditCardSvc:  SaveSvc<CreditCardBoughtDTO>,
    SearchSvc<CreditCardBoughtDTO>, IGetPeriodsServices  {
        public fun getTotalQuoteTC():BigDecimal
        public fun getRecurrentPendingQuotes(key: Int, cutOff: LocalDateTime):List<CreditCardBoughtDTO>

        fun endingRecurrentPayment(idBought: Int,cutOff:LocalDateTime):Boolean

        fun getLastAvailableQuotesTC():List<QuoteCreditCard>

        fun getDataToGraphStats(codCreditCard:Int, cutOff: LocalDateTime):List<Pair<String,Double>>
}