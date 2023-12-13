package co.japl.android.finances.services.interfaces

import co.japl.android.finances.services.dto.InputDTO
import java.math.BigDecimal
import java.time.LocalDate

interface IInputSvc: SaveSvc<InputDTO>,ISaveSvc<InputDTO>  {

    public fun getTotalInputs():BigDecimal

    public fun getTotalInputsSemestral(): BigDecimal

    fun getAllValid(date:LocalDate):List<InputDTO>
}