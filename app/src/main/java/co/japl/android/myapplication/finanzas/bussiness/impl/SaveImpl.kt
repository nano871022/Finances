package co.japl.android.myapplication.bussiness.impl

import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.bussiness.DTO.CalcDB
import co.japl.android.myapplication.bussiness.DTO.CalcDTO
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.bussiness.interfaces.Calc
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.bussiness.mapping.CalcMap
import co.japl.android.myapplication.finanzas.bussiness.DTO.PeriodDTO
import co.japl.android.myapplication.utils.DatabaseConstants
import com.google.gson.Gson
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

class SaveImpl(override var dbConnect: SQLiteOpenHelper) : SaveSvc<CalcDTO> {
    private val COLUMNS_CALC = arrayOf(BaseColumns._ID
        ,CalcDB.CalcEntry.COLUMN_ALIAS
        ,CalcDB.CalcEntry.COLUMN_TYPE
        ,CalcDB.CalcEntry.COLUMN_INTEREST
        ,CalcDB.CalcEntry.COLUMN_PERIOD
        ,CalcDB.CalcEntry.COLUMN_QUOTE_CREDIT
        ,CalcDB.CalcEntry.COLUMN_VALUE_CREDIT
        ,CalcDB.CalcEntry.COLUMN_INTEREST_VALUE
        ,CalcDB.CalcEntry.COLUMN_CAPITAL_VALUE)

    override fun save(calc: CalcDTO): Long {
        val db = dbConnect.writableDatabase
        val dto = CalcMap().mapping(calc)
        return db?.insert(CalcDB.CalcEntry.TABLE_NAME,null,dto)!!
    }

    override  fun getAll():List<CalcDTO>{
        val db = dbConnect.readableDatabase

        val cursor = db.query(CalcDB.CalcEntry.TABLE_NAME,COLUMNS_CALC,null,null,null,null,null)
        val items = mutableListOf<CalcDTO>()
        with(cursor){
            while(moveToNext()){
                items.add(CalcMap().mapping(this))
            }
        }
        return items
    }

    override fun delete(id:Int):Boolean{
        val db = dbConnect.writableDatabase
        return db.delete(CalcDB.CalcEntry.TABLE_NAME, DatabaseConstants.SQL_DELETE_CALC_ID, arrayOf(id.toString())) > 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(id: Int): Optional<CalcDTO> {
        val db = dbConnect.writableDatabase
        val cursor = db.query(
            CalcDB.CalcEntry.TABLE_NAME,
            COLUMNS_CALC,
            " id = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        with(cursor) {
            while (moveToNext()) {
                return Optional.ofNullable(CalcMap().mapping(this))
            }
        }
        return Optional.empty()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun backup(pathFile: String) {
        val values = getAll()
        val path = Paths.get(pathFile)
        Files.newBufferedWriter(path, Charset.defaultCharset()).use { it.write(Gson().toJson(values)) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun restoreBackup(pathFile: String) {
        val path = Paths.get(pathFile)
        val list = Files.newBufferedReader(path, Charset.defaultCharset()).use { Gson().fromJson(it,List::class.java) } as List<CalcDTO>
        list.forEach(this::save)
    }
}