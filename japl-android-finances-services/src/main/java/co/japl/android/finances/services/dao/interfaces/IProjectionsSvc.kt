package co.japl.android.finances.services.dao.interfaces

import co.japl.android.finances.services.dto.ProjectionDTO
import co.japl.android.finances.services.interfaces.ISaveSvc
import co.japl.android.finances.services.interfaces.SaveSvc
import java.math.BigDecimal
import java.time.LocalDate

interface IProjectionsSvc: SaveSvc<ProjectionDTO>, ISaveSvc<ProjectionDTO> {
    fun getAllActive():List<ProjectionDTO>
}