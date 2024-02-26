package co.japl.android.finances.services.interfaces

import co.japl.android.finances.services.dto.ExtraValueAmortizationCreditDTO

interface IExtraValueAmortizationCreditSvc : SaveSvc<ExtraValueAmortizationCreditDTO> {
    fun createNew(code:Int, nbrQuote:Long, value:Double): Boolean

    fun getAll(code:Int):List<ExtraValueAmortizationCreditDTO>
}