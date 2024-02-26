package co.japl.android.finances.services.interfaces

import co.japl.android.finances.services.dto.ProjectionDTO
import java.math.BigDecimal
import java.time.LocalDate

interface IProjectionsSvc: SaveSvc<ProjectionDTO>, ISaveSvc<ProjectionDTO> {

    fun getClose():Triple<LocalDate,Int,BigDecimal>
    fun getFar():Triple<LocalDate,Int,BigDecimal>
    fun getTotal():Pair<Int,BigDecimal>
    fun getAllActive():List<ProjectionDTO>
}