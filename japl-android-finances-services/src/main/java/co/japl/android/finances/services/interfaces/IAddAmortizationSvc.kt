package co.japl.android.finances.services.interfaces

import co.japl.android.finances.services.dto.AddAmortizationDTO

interface IAddAmortizationSvc : SaveSvc<AddAmortizationDTO> {
    fun createNew(code:Int, nbrQuote:Long, value:Double): Boolean

    fun getAll(code:Int):List<AddAmortizationDTO>
}