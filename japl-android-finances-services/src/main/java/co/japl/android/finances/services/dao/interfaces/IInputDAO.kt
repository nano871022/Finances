package co.japl.android.finances.services.dao.interfaces

import co.japl.android.finances.services.dto.InputDTO
import co.japl.android.finances.services.interfaces.ISaveSvc
import co.japl.android.finances.services.interfaces.SaveSvc
import java.math.BigDecimal
import java.time.LocalDate

interface IInputDAO: SaveSvc<InputDTO>,ISaveSvc<InputDTO>  {

    public fun getTotalInputs():BigDecimal

    public fun getTotalInputsSemestral(): BigDecimal

    fun getAllValid(accountCode:Int,date:LocalDate):List<InputDTO>
}