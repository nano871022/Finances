package co.japl.android.myapplication.finanzas.bussiness.interfaces

import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.AddAmortizationDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.ExtraValueAmortizationCreditDTO

interface IExtraValueAmortizationCreditSvc : SaveSvc<ExtraValueAmortizationCreditDTO> {
    fun createNew(code:Int, nbrQuote:Long, value:Double): Boolean

    fun getAll(code:Int):List<ExtraValueAmortizationCreditDTO>
}