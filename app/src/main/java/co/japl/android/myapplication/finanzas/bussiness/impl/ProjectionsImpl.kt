package co.japl.android.myapplication.finanzas.bussiness.impl

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.finanzas.bussiness.DTO.ProjectionDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.ProjectionDTO
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IProjectionsSvc
import co.japl.android.myapplication.finanzas.bussiness.mapping.ProjectionMap
import co.japl.android.myapplication.utils.DatabaseConstants
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Period
import java.util.*
import javax.inject.Inject

class ProjectionsImpl @Inject constructor(override var dbConnect: SQLiteOpenHelper) : IProjectionsSvc {
     val COLUMNS = arrayOf(
         BaseColumns._ID,
        ProjectionDB.Entry.COLUMN_NAME,
         ProjectionDB.Entry.COLUMN_VALUE,
         ProjectionDB.Entry.COLUMN_TYPE,
         ProjectionDB.Entry.COLUMN_ACTIVE,
         ProjectionDB.Entry.COLUMN_QUOTE,
         ProjectionDB.Entry.COLUMN_DATE_CREATE,
         ProjectionDB.Entry.COLUMN_DATE_END
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getClose(): Triple<LocalDate, Int, BigDecimal> {
            val list = getAllActive().filter{ LocalDate.now() <= it.end }
            if(list.size > 0){
                val record = list[0]
                val date = record.end
                val quote = record.quote
                val type = record.type
                val months = getMonths(type)
                val faltantes = Period.between(LocalDate.now(),date).toTotalMonths()
                val month = months - faltantes
                var saved = quote * month.toBigDecimal()
                if(saved < BigDecimal.ZERO){
                    saved = BigDecimal.ZERO
                }
                return Triple(date,faltantes.toInt(),saved)
            }
        return Triple(LocalDate.now(),0,BigDecimal.ZERO)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getFar(): Triple<LocalDate, Int, BigDecimal> {
        val list = getAllActive().filter{ LocalDate.now() <= it.end }
            if(list.size > 0){
                val record = list[list.size-1]
                val date =record.end
                val quote = record.quote
                val type = record.type
                val months = getMonths(type)
                val faltantes = Period.between(LocalDate.now(),date).toTotalMonths()
                val month = months - faltantes
                var saved = quote * month.toBigDecimal()
                if(saved < BigDecimal.ZERO){
                    saved = BigDecimal.ZERO
                }
                return Triple(date,faltantes.toInt(),saved)
            }
        return Triple(LocalDate.now(),0,BigDecimal.ZERO)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getTotal(): Pair<Int, BigDecimal> {
        val items = getAllActive().filter{ LocalDate.now() <= it.end }
        return Pair(items.size,items.sumOf {
            val months = getMonths(it.type)
            var value = it.quote * (months-Period.between(LocalDate.now(),it.end).toTotalMonths()).toBigDecimal()
            if(value < BigDecimal.ZERO){
                value = BigDecimal.ZERO
            }
            value
        })
    }

    private fun getMonths(type:String):Int{
        return when(type.toLowerCase()){
            "trimestral" -> 3
            "cuatrimestral" ->4
            "semestral" ->6
            "anual" ->12
            else -> 0
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAllActive(): List<ProjectionDTO> {
        val db = dbConnect.readableDatabase
        val cursor = db.query(ProjectionDB.Entry.TABLE_NAME,COLUMNS,"${ProjectionDB.Entry.COLUMN_ACTIVE}=1",
            null,null,null,"${ProjectionDB.Entry.COLUMN_DATE_END} ASC")
        val items = mutableListOf<ProjectionDTO>()
        val mapper = ProjectionMap()
        with(cursor){
            while(moveToNext()){
                items.add(mapper.mapping(cursor))
            }
        }
        items.sortBy { it.end }
        return items
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getTotalSavedAndQuote(): Pair<BigDecimal, BigDecimal> {
        val values = getAllActive()
        val quote = values.sumOf { it.quote }
        val saved = values.sumOf { it.quote * (getMonths(it.type) - Period.between(LocalDate.now(),it.end).toTotalMonths()).toBigDecimal() }
        return Pair(saved,quote)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun save(dto: ProjectionDTO): Long {
        val db = dbConnect.writableDatabase
        val content: ContentValues? = ProjectionMap().mapping(dto)
        return if(dto.id > 0){
            db?.update(ProjectionDB.Entry.TABLE_NAME,content,"${BaseColumns._ID}=?", arrayOf(dto.id.toString()))?.toLong() ?: 0
        }else {
            db?.insert(ProjectionDB.Entry.TABLE_NAME, null, content) ?: 0
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAll(): List<ProjectionDTO> {
        val db = dbConnect.readableDatabase
        val cursor = db.query(ProjectionDB.Entry.TABLE_NAME,COLUMNS,null,null,null,null,null)
        val items = mutableListOf<ProjectionDTO>()
        val mapper = ProjectionMap()
        with(cursor){
            while(moveToNext()){
                items.add(mapper.mapping(cursor))
            }
        }
        return items
    }

    override fun delete(id: Int): Boolean {
        val db = dbConnect.writableDatabase
        return db.delete(
            ProjectionDB.Entry.TABLE_NAME, DatabaseConstants.SQL_DELETE_CALC_ID,
            arrayOf(id.toString())) > 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(id: Int): Optional<ProjectionDTO> {
        val db = dbConnect.readableDatabase

        val cursor = db.query(
            ProjectionDB.Entry.TABLE_NAME,COLUMNS,"${BaseColumns._ID} = ?",
            arrayOf(id.toString()),null,null,null)
        val mapper = ProjectionMap()
        with(cursor){
            while(moveToNext()){
                return Optional.ofNullable(mapper.mapping(cursor))
            }
        }
        return Optional.empty()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(values: ProjectionDTO): List<ProjectionDTO> {
        val db = dbConnect.readableDatabase
        val cursor = db.query(ProjectionDB.Entry.TABLE_NAME,COLUMNS,null,null,null,null,null)
        val items = mutableListOf<ProjectionDTO>()
        val mapper = ProjectionMap()
        with(cursor){
            while(moveToNext()){
                items.add(mapper.mapping(cursor))
            }
        }
        return items
    }

    override fun backup(path: String) {
    }

    override fun restoreBackup(path: String) {
    }
}