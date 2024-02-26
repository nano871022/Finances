package co.japl.android.finances.services.interfaces

import co.japl.android.finances.services.dto.ExtraValueAmortizationQuoteCreditCardDTO

interface IExtraValueAmortizationQuoteCreditCardSvc : SaveSvc<ExtraValueAmortizationQuoteCreditCardDTO> {
    fun createNew(code:Int, nbrQuote:Long, value:Double): Boolean

    fun getAll(code:Int):List<ExtraValueAmortizationQuoteCreditCardDTO>
}