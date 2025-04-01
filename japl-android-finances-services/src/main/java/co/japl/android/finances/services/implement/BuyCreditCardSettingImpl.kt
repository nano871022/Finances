package co.japl.android.finances.services.implement

import android.database.CursorWindow
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.finances.services.dto.BuyCreditCardSettingDTO
import co.japl.android.finances.services.dto.BuyCreditCardSettingDB
import co.japl.android.finances.services.mapping.BuyCreditCardSettingMap
import co.japl.android.finances.services.interfaces.IBuyCreditCardSettingSvc
import co.japl.android.finances.services.utils.DatabaseConstants
import java.util.*
import javax.inject.Inject

class BuyCreditCardSettingImpl @Inject constructor(override var dbConnect: SQLiteOpenHelper) : IBuyCreditCardSettingSvc {
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
        Log.d(this.javaClass.name,"<<<=== STARTING::getAll ")
        val list = mutableListOf<BuyCreditCardSettingDTO>()
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
            return list.also { Log.d(this.javaClass.name, "<<<=== ENDING::getAll Size: ${it.size}")}
    }

    override fun delete(id: Int): Boolean {
        val db = dbConnect.writableDatabase
        return db.delete(BuyCreditCardSettingDB.Entry.TABLE_NAME, DatabaseConstants.SQL_DELETE_CALC_ID,
            arrayOf(id.toString())) > 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(id: Int): Optional<BuyCreditCardSettingDTO> {
        Log.d(javaClass.name,"<<<=== STARTING::get Id: $id")
        /*
        val field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
        field.isAccessible = true
        field.set(null, 800 * 1024 * 1024)
         */
    try{
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
                return Optional.ofNullable(mapper.mapping(this)).also{Log.d(javaClass.name,"<<<=== ENDING::get $it")}
            }
        }
    }catch(e:SQLiteException){
        Log.e(this.javaClass.name, e.message, e)
    }
           Log.d(javaClass.name,"<<<=== ENDING::get EMPTY")
        return Optional.empty()
    }

    override fun backup(path: String) {
        TODO("Not yet implemented")
    }

    override fun restoreBackup(path: String) {
        TODO("Not yet implemented")
    }
}