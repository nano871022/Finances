package co.japl.android.finances.services.implement

import android.database.CursorWindow
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.finances.services.core.Caching
import co.japl.android.finances.services.dto.CreditCardDB
import co.japl.android.finances.services.dto.CreditCardDTO
import co.japl.android.finances.services.mapping.CreditCardMap
import co.japl.android.finances.services.interfaces.ICreditCardSvc
import co.japl.android.finances.services.utils.DatabaseConstants
import com.google.gson.Gson
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import javax.inject.Inject
import kotlin.io.path.isReadable
import kotlin.io.path.isWritable

class   CreditCardImpl @Inject constructor(override var dbConnect: SQLiteOpenHelper) : ICreditCardSvc {
    private val COLUMNS = arrayOf(BaseColumns._ID,
                                  CreditCardDB.CreditCardEntry.COLUMN_NAME,
                                  CreditCardDB.CreditCardEntry.COLUMN_MAX_QUOTES,
                                  CreditCardDB.CreditCardEntry.COLUMN_CUT_OFF_DAY,
                                  CreditCardDB.CreditCardEntry.COLUMN_WARNING_VALUE,
                                  CreditCardDB.CreditCardEntry.COLUMN_STATUS,
                                  CreditCardDB.CreditCardEntry.COLUMN_INTEREST_1Q,
                                  CreditCardDB.CreditCardEntry.COLUMN_INTEREST_1NOTQ,
                                  CreditCardDB.CreditCardEntry.COLUMN_CREATE_DATE)
    private val mapper = CreditCardMap()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun save(dto: CreditCardDTO): Long {
        val db = dbConnect.writableDatabase
        val columns = mapper.mapping(dto)
        if(dto.id <= 0){
            return db.insert(CreditCardDB.CreditCardEntry.TABLE_NAME,null,columns)
        }
        return  db.update(CreditCardDB.CreditCardEntry.TABLE_NAME ,mapper.mapping(dto),"${BaseColumns._ID} = ?", arrayOf(dto.id.toString())  ).toLong()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAll(): List<CreditCardDTO> {
        Log.d(this.javaClass.name,"<<<=== getAll - Start")
        val list = mutableListOf<CreditCardDTO>()
        try {
            val db = dbConnect.writableDatabase
            val cursor = db.query(
                CreditCardDB.CreditCardEntry.TABLE_NAME,
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
            return list.also{Log.d(this.javaClass.name, "<<<=== getAll - End Size: ${it.size}")}
        }catch(e:SQLiteException){
            Log.e(this.javaClass.name,e.message,e)
            return list
        }
    }

    override fun delete(id: Int): Boolean {
        val db = dbConnect.writableDatabase
        return db.delete(CreditCardDB.CreditCardEntry.TABLE_NAME, DatabaseConstants.SQL_DELETE_CALC_ID,
            arrayOf(id.toString())) > 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(id: Int): Optional<CreditCardDTO> = Caching("CreditCardDAO|get|$id"){
        val db = dbConnect.writableDatabase
        val cursor = db.query(
            CreditCardDB.CreditCardEntry.TABLE_NAME,
            COLUMNS,
            " ${BaseColumns._ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        with(cursor) {
            if (moveToFirst()) {
                return@Caching Optional.ofNullable(mapper.mapping(this))
            }
        }
        return@Caching Optional.empty()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun backup(pathFile: String) {
        val values = getAll()
        val path = Paths.get(pathFile)
        Log.v(this.javaClass.name,"Path: $path Writable: ${path.isWritable()}")
        if(path.isWritable()) {
            Files.newBufferedWriter(path, Charset.defaultCharset())
                .use { it.write(Gson().toJson(values)) }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun restoreBackup(pathFile: String) {
        val path = Paths.get(pathFile)
        Log.v(this.javaClass.name,"Path: $path Readable: ${path.isReadable()}")
        if(path.isReadable()) {
            val list = Files.newBufferedReader(path, Charset.defaultCharset())
                .use { Gson().fromJson(it, List::class.java) } as List<CreditCardDTO>
            list.forEach(this::save)
        }
    }

}