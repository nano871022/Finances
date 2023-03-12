package co.japl.android.myapplication.bussiness.impl

import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.bussiness.DTO.CreditCardDTO
import co.japl.android.myapplication.bussiness.DTO.TaxDB
import co.japl.android.myapplication.bussiness.DTO.TaxDTO
import co.japl.android.myapplication.bussiness.interfaces.IMapper
import co.japl.android.myapplication.bussiness.interfaces.ITaxSvc
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.bussiness.mapping.TaxMap
import co.japl.android.myapplication.finanzas.utils.TaxEnum
import co.japl.android.myapplication.utils.DatabaseConstants
import com.google.gson.Gson
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

class TaxImpl(override var dbConnect: SQLiteOpenHelper) :  SaveSvc<TaxDTO>,ITaxSvc{
    private val COLUMNS = arrayOf(BaseColumns._ID,
                                  TaxDB.TaxEntry.COLUMN_TAX,
                                  TaxDB.TaxEntry.COLUMN_MONTH,
                                  TaxDB.TaxEntry.COLUMN_YEAR,
                                  TaxDB.TaxEntry.COLUMN_status,
                                  TaxDB.TaxEntry.COLUMN_COD_CREDIT_CARD,
                                  TaxDB.TaxEntry.COLUMN_CREATE_DATE,
                                  TaxDB.TaxEntry.COLUMN_KIND,
                                  TaxDB.TaxEntry.COLUMN_PERIOD)
    private val mapper:IMapper<TaxDTO> = TaxMap()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun save(dto: TaxDTO): Long {
        Log.d(this.javaClass.name,"<<<== save - Start")
        val db = dbConnect.writableDatabase
        val columns = mapper.mapping(dto)
        return db.insert(TaxDB.TaxEntry.TABLE_NAME,null,columns).also {
            Log.d(this.javaClass.name,"<<<=== save - resposnse $it $columns End ")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAll(): List<TaxDTO> {
        Log.i(this.javaClass.name,"<<<=== getAll - Start")
        val list = mutableListOf<TaxDTO>()
        try {
            val db = dbConnect.writableDatabase
            val cursor = db.query(
                TaxDB.TaxEntry.TABLE_NAME,
                COLUMNS,
                null,
                null,
                null,
                null,
                null
            )
            with(cursor) {
                while (moveToNext()) {
                    list.add(mapper.mapping(this))
                }
            }
            return list
        }catch(e:SQLiteException){
            Log.e(this.javaClass.name,e.message,e)
            return list
        }finally {
            Log.i(this.javaClass.name, "<<<=== getAll - End")
        }
    }

    override fun delete(id: Int): Boolean {
        Log.d(this.javaClass.name,"<<<=== delete - Start - $id")
        val db = dbConnect.writableDatabase
        return (db.delete(TaxDB.TaxEntry.TABLE_NAME, DatabaseConstants.SQL_DELETE_CALC_ID,
            arrayOf(id.toString())) > 0).also { Log.d(this.javaClass.name, "<<<=== delete - End - $id")}
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(id: Int): Optional<TaxDTO> {
        val db = dbConnect.writableDatabase
        val cursor = db.query(
            TaxDB.TaxEntry.TABLE_NAME,
            COLUMNS,
            " id = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        with(cursor) {
            while (moveToNext()) {
                return Optional.ofNullable(mapper.mapping(this))
            }
        }
        return Optional.empty()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun get(month:Int, year:Int,kind:TaxEnum):Optional<TaxDTO>{
        Log.d(this.javaClass.name,"<<<=== Start get - $month , $year")
        try {
            val db = dbConnect.writableDatabase
            val cursor = db.query(
                TaxDB.TaxEntry.TABLE_NAME,
                COLUMNS,
                " ${TaxDB.TaxEntry.COLUMN_MONTH} = ? and ${TaxDB.TaxEntry.COLUMN_YEAR} = ? and ${TaxDB.TaxEntry.COLUMN_KIND} = ?",
                arrayOf(month.toString(), year.toString(), kind.ordinal.toString()),
                null,
                null,
                null
            )
            with(cursor) {
                while (moveToNext()) {
                    return Optional.ofNullable(mapper.mapping(this)).also {
                        Log.d(this.javaClass.name,"response $it")
                    }
                }
            }
            return Optional.empty()
        }finally{
            Log.d(this.javaClass.name,"<<<=== End - get $month , $year")
        }
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
        val list = Files.newBufferedReader(path, Charset.defaultCharset()).use { Gson().fromJson(it,List::class.java) } as List<TaxDTO>
        list.forEach(this::save)
    }
}