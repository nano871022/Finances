package co.japl.android.finances.services.dao.interfaces

import co.japl.android.finances.services.dto.AddAmortizationDTO
import co.japl.android.finances.services.interfaces.SaveSvc

interface IAddAmortizationDAO : SaveSvc<AddAmortizationDTO> {
    fun createNew(code:Int, nbrQuote:Long, value:Double): Boolean

    fun getAll(code:Int):List<AddAmortizationDTO>
}