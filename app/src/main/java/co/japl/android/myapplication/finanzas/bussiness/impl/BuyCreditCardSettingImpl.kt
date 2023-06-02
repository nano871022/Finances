package co.japl.android.myapplication.finanzas.bussiness.impl

import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.bussiness.DTO.BuyCreditCardSettingDTO
import co.japl.android.myapplication.bussiness.DTO.BuyCreditCardSettingDB
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.bussiness.mapping.BuyCreditCardSettingMap
import co.japl.android.myapplication.bussiness.queries.BuyCreditCardSettingQuery
import co.japl.android.myapplication.utils.DatabaseConstants
import java.util.*

class BuyCreditCardSettingImpl(override var dbConnect: SQLiteOpenHelper) : SaveSvc<BuyCreditCardSettingDTO> {
    private val COLUMNS = arrayOf(
        BaseColumns._ID,
        BuyCreditCardSettingDB.Entry.COLUMN_COD_BUY_CREDIT_CARD,
        BuyCreditCardSettingDB.Entry.COLUMN_COD_CREDIT_CARD_SETTING,
        BuyCreditCardSettingDB.Entry.COLUMN_ACTIVE,
        BuyCreditCardSettingDB.Entry.COLUMN_CREATE_DATE)
    private val mapper = BuyCreditCardSettingMap()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun save(dto: BuyCreditCardSettingDTO): Long {
            val db = dbConnect.writableDatabase
            val columns = mapper.mapping(dto)
            if (dto.id <= 0) {
                return db.insert(BuyCreditCardSettingDB.Entry.TABLE_NAME, null, columns)
            }
            return db.update(
                BuyCreditCardSettingDB.Entry.TABLE_NAME,
                mapper.mapping(dto),
                "_id = ?",
                arrayOf(dto.id.toString())
            ).toLong().also { Log.v(this.javaClass.name,"Buy Credicard Setting save records $dto")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAll(): List<BuyCreditCardSettingDTO> {
        Log.d(this.javaClass.name,"<<<=== getAll - Start")
        val list = mutableListOf<BuyCreditCardSettingDTO>()
        try {
            val db = dbConnect.writableDatabase
            val cursor = db.query(
                BuyCreditCardSettingDB.Entry.TABLE_NAME,
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
        }catch(e: SQLiteException){
            Log.e(this.javaClass.name,e.message,e)
            return list
        }finally {
            Log.d(this.javaClass.name, "<<<=== getAll - End")
        }
    }

    override fun delete(id: Int): Boolean {
        val db = dbConnect.writableDatabase
        return db.delete(BuyCreditCardSettingDB.Entry.TABLE_NAME, DatabaseConstants.SQL_DELETE_CALC_ID,
            arrayOf(id.toString())) > 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(id: Int): Optional<BuyCreditCardSettingDTO> {
        val db = dbConnect.writableDatabase
        val cursor = db.query(
            BuyCreditCardSettingDB.Entry.TABLE_NAME,
            COLUMNS,
            " ${BuyCreditCardSettingDB.Entry.COLUMN_COD_BUY_CREDIT_CARD} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        with(cursor) {
            if (moveToNext()) {
                return Optional.ofNullable(mapper.mapping(this))
            }
        }
        return Optional.empty()
    }

    override fun backup(path: String) {
        TODO("Not yet implemented")
    }

    override fun restoreBackup(path: String) {
        TODO("Not yet implemented")
    }
}