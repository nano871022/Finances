package co.japl.android.myapplication.finanzas.bussiness.interfaces

import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.ProjectionDTO
import java.math.BigDecimal
import java.time.LocalDate

interface IProjectionsSvc: SaveSvc<ProjectionDTO>, ISaveSvc<ProjectionDTO> {

    fun getClose():Triple<LocalDate,Int,BigDecimal>
    fun getFar():Triple<LocalDate,Int,BigDecimal>
    fun getTotal():Pair<Int,BigDecimal>
    fun getAllActive():List<ProjectionDTO>
}