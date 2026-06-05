package co.japl.android.finances.services.dao.interfaces

import co.com.japl.finances.iports.dtos.CalcDTO
import co.japl.android.finances.services.interfaces.SaveSvc

interface ISimulatorCreditDAO: SaveSvc<CalcDTO> {
    fun getByVariable():List<CalcDTO>
    fun getByFix():List<CalcDTO>

}