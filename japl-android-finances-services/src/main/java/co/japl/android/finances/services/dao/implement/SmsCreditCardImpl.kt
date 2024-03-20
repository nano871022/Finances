package co.japl.android.finances.services.dao.implement

import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.japl.android.finances.services.dao.interfaces.ISmsCreditCardDAO
import co.japl.android.finances.services.entities.SmsCreditCard
import co.japl.android.finances.services.entities.SmsCreditCardDB
import co.japl.android.finances.services.mapping.SmsCreditCardMap
import java.util.Optional
import javax.inject.Inject

class SmsCreditCardImpl @Inject constructor(override var dbConnect: SQLiteOpenHelper) : ISmsCreditCardDAO {

    val COLUMNS = arrayOf(
        BaseColumns._ID,
        SmsCreditCardDB.Entry.COLUMN_CODE_CREDIT_CARD,
        SmsCreditCardDB.Entry.COLUMN_PHONE_NUMBER,
        SmsCreditCardDB.Entry.COLUMN_PATTERN,
        SmsCreditCardDB.Entry.COLUMN_KIND_INTEREST_RATE,
        SmsCreditCardDB.Entry.COLUMN_CREATE_DATE,
        SmsCreditCardDB.Entry.COLUMN_ACTIVE
    )
    override fun getByCreditCardAndKindInterest(
        codeCreditCard: Int,
        kind: KindInterestRateEnum
    ): List<SmsCreditCard> {
        val list = mutableListOf<SmsCreditCard>()
        val db = dbConnect.readableDatabase
        val cursor = db.query(
            SmsCreditCardDB.Entry.TABLE_NAME,
            COLUMNS,
            "${SmsCreditCardDB.Entry.COLUMN_CODE_CREDIT_CARD}=? AND ${SmsCreditCardDB.Entry.COLUMN_KIND_INTEREST_RATE}=? AND ${SmsCreditCardDB.Entry.COLUMN_ACTIVE}=1",
            arrayOf(codeCreditCard.toString(), kind.getCode().toString()),
            null,
            null,
            null
        )
        with(cursor){
            while (moveToNext()){
                list.add(SmsCreditCardMap().mapping(cursor))
            }
        }
        return list
    }

    override fun save(dto: SmsCreditCard): Long {
        val db = dbConnect.readableDatabase
        val content = SmsCreditCardMap().mapping(dto)
        return dto.takeIf { it.id!! > 0 }?.let{
            db.update(SmsCreditCardDB.Entry.TABLE_NAME,content,"${BaseColumns._ID} = ?", arrayOf(it.id.toString())).toLong()
        }?:db.insert(SmsCreditCardDB.Entry.TABLE_NAME,null,content)
    }

    override fun getAll(): List<SmsCreditCard> {
        val list = mutableListOf<SmsCreditCard>()
        val mapper = SmsCreditCardMap()
        val db = dbConnect.readableDatabase
        val cursor = db.query(SmsCreditCardDB.Entry.TABLE_NAME,COLUMNS,null,null,null,null,null)
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
        return db.delete(SmsCreditCardDB.Entry.TABLE_NAME, "${BaseColumns._ID} = ?", arrayOf(id.toString())) > 0
    }

    override fun get(id: Int): Optional<SmsCreditCard> {
        require(id > 0){"Id must be > 0"}
        val mapper = SmsCreditCardMap()
        val db = dbConnect.readableDatabase
        val cursor = db.query(
            SmsCreditCardDB.Entry.TABLE_NAME,COLUMNS,"${BaseColumns._ID} = ?",
            arrayOf(id.toString()),null,null,null
        )
        with(cursor){
            while(moveToNext()){
                mapper.mapping(cursor).let { return Optional.of(it) }
            }
        }
        return Optional.empty()
    }

    override fun get(values: SmsCreditCard): List<SmsCreditCard> {
        val list = mutableListOf<SmsCreditCard>()
        val mapper = SmsCreditCardMap()
        val db = dbConnect.readableDatabase
        val cursor = db.query(SmsCreditCardDB.Entry.TABLE_NAME,COLUMNS,
            "${SmsCreditCardDB.Entry.COLUMN_CODE_CREDIT_CARD} =?",
            arrayOf(values.codeCreditCard.toString()),null,null,null)
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