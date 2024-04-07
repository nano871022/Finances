package co.japl.android.finances.services.dao.implement

import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import co.japl.android.finances.services.dao.interfaces.ISmsPaidDAO
import co.japl.android.finances.services.entities.SmsPaid
import co.japl.android.finances.services.entities.SmsPaidDB
import co.japl.android.finances.services.mapping.SmsPaidMap
import java.util.Optional
import javax.inject.Inject

class SmsPaidImpl @Inject constructor(override var dbConnect: SQLiteOpenHelper) : ISmsPaidDAO {

    val COLUMNS = arrayOf(
        BaseColumns._ID,
        SmsPaidDB.Entry.COLUMN_CODE_ACCOUNT,
        SmsPaidDB.Entry.COLUMN_PHONE_NUMBER,
        SmsPaidDB.Entry.COLUMN_PATTERN,
        SmsPaidDB.Entry.COLUMN_CREATE_DATE,
        SmsPaidDB.Entry.COLUMN_ACTIVE
    )
    override fun getByAccount(
        codeCreditCard: Int
    ): List<SmsPaid> {
        val list = mutableListOf<SmsPaid>()
        val db = dbConnect.readableDatabase
        val cursor = db.query(
            SmsPaidDB.Entry.TABLE_NAME,
            COLUMNS,
            "${SmsPaidDB.Entry.COLUMN_CODE_ACCOUNT}=? AND ${SmsPaidDB.Entry.COLUMN_ACTIVE}=1",
            arrayOf(codeCreditCard.toString()),
            null,
            null,
            null
        )
        with(cursor){
            while (moveToNext()){
                list.add(SmsPaidMap().mapping(cursor))
            }
        }
        return list
    }

    override fun save(dto: SmsPaid): Long {
        val db = dbConnect.readableDatabase
        val content = SmsPaidMap().mapping(dto)
        return dto.takeIf { it.id!! > 0 }?.let{
            db.update(SmsPaidDB.Entry.TABLE_NAME,content,"${BaseColumns._ID} = ?", arrayOf(it.id.toString())).toLong()
        }?:db.insert(SmsPaidDB.Entry.TABLE_NAME,null,content)
    }

    override fun getAll(): List<SmsPaid> {
        val list = mutableListOf<SmsPaid>()
        val mapper = SmsPaidMap()
        val db = dbConnect.readableDatabase
        val cursor = db.query(SmsPaidDB.Entry.TABLE_NAME,COLUMNS,null,null,null,null,null)
        with(cursor){
            while (moveToPrevious()){
                mapper.mapping(cursor).let(list::add)
            }
        }
        return list
    }

    override fun delete(id: Int): Boolean {
        require(id > 0){"Id must be > 0"}
        val db = dbConnect.readableDatabase
        return db.delete(SmsPaidDB.Entry.TABLE_NAME, "${BaseColumns._ID} = ?", arrayOf(id.toString())) > 0
    }

    override fun get(id: Int): Optional<SmsPaid> {
        require(id > 0){"Id must be > 0"}
        val mapper = SmsPaidMap()
        val db = dbConnect.readableDatabase
        val cursor = db.query(
            SmsPaidDB.Entry.TABLE_NAME,COLUMNS,"${BaseColumns._ID} = ?",
            arrayOf(id.toString()),null,null,null
        )
        with(cursor){
            while(moveToNext()){
                mapper.mapping(cursor).let { return Optional.of(it) }
            }
        }
        return Optional.empty()
    }

    override fun get(values: SmsPaid): List<SmsPaid> {
        val list = mutableListOf<SmsPaid>()
        val mapper = SmsPaidMap()
        val db = dbConnect.readableDatabase
        val cursor = db.query(SmsPaidDB.Entry.TABLE_NAME,COLUMNS,
            "${SmsPaidDB.Entry.COLUMN_CODE_ACCOUNT} =?",
            arrayOf(values.codeAccount.toString()),null,null,null)
        with(cursor){
            while (moveToNext()){
                mapper.mapping(cursor).let(list::add)
            }
        }
        return list
    }

    override fun backup(path: String) {
        TODO("Not yet implemented")
    }

    override fun restoreBackup(path: String) {
        TODO("Not yet implemented")
    }


}