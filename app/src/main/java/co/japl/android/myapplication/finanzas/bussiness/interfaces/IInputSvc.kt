package co.japl.android.myapplication.finanzas.bussiness.interfaces

import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.InputDTO
import java.math.BigDecimal

interface IInputSvc: SaveSvc<InputDTO>,ISaveSvc<InputDTO>  {

    public fun getTotalInputs():BigDecimal
}