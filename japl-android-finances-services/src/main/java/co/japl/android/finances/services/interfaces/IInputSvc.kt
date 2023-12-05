package co.japl.android.finances.services.interfaces

import co.japl.android.finances.services.dto.InputDTO
import java.math.BigDecimal

interface IInputSvc: SaveSvc<InputDTO>,ISaveSvc<InputDTO>  {

    public fun getTotalInputs():BigDecimal

    public fun getTotalInputsSemestral(): BigDecimal
}