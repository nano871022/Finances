package co.japl.android.finances.services.implement

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import androidx.annotation.RequiresApi
import co.japl.android.finances.services.dto.*
import co.japl.android.finances.services.interfaces.IAccountSvc
import co.japl.android.finances.services.mapping.AccountMap
import co.japl.android.finances.services.utils.DatabaseConstants
import java.util.*
import javax.inject.Inject

class AccountImpl @Inject constructor(override var dbConnect: SQLiteOpenHelper) : IAccountSvc {
    val COLUMNS = arrayOf(
        BaseColumns._ID,
        AccountDB.Entry.COLUMN_DATE_CREATE,
        AccountDB.Entry.COLUMN_NAME,
        AccountDB.Entry.COLUMN_ACTIVE,
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(values: AccountDTO): List<AccountDTO> {
        val db = dbConnect.readableDatabase
        val cursor = db.query(AccountDB.Entry.TABLE_NAME,COLUMNS,"",arrayOf(),null,null,null,null)
        val items = mutableListOf<AccountDTO>()
        val mapper = AccountMap()
        with(cursor){
            while(moveToNext()){
                items.add(mapper.mapping(cursor))
            }
        }
        return items
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun save(dto: AccountDTO): Long {
        val db = dbConnect.writableDatabase
        val content: ContentValues? = AccountMap().mapping(dto)
        return if(dto.id > 0){
            db?.update(AccountDB.Entry.TABLE_NAME,content,"_id=?", arrayOf(dto.id.toString()))?.toLong() ?: 0
        }else {
            db?.insert(AccountDB.Entry.TABLE_NAME, null, content) ?: 0
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAll(): List<AccountDTO> {
        val db = dbConnect.readableDatabase
        val cursor = db.query(AccountDB.Entry.TABLE_NAME,COLUMNS,null,null,null,null,null)
        val items = mutableListOf<AccountDTO>()
        val mapper = AccountMap()
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
            AccountDB.Entry.TABLE_NAME, DatabaseConstants.SQL_DELETE_CALC_ID,
            arrayOf(id.toString())) > 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(id: Int): Optional<AccountDTO> {
        val db = dbConnect.readableDatabase

        val cursor = db.query(
            AccountDB.Entry.TABLE_NAME,COLUMNS,"_id = ?",
            arrayOf(id.toString()),null,null,null)
        val mapper = AccountMap()
        with(cursor){
            while(moveToNext()){
                return Optional.ofNullable(mapper.mapping(cursor))
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