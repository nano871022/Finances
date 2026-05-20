package co.japl.android.finances.services.dao.implement

import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import co.com.japl.finances.iports.dtos.EmailCreditCardDTO
import co.japl.android.finances.services.dao.interfaces.IEmailCreditCardDAO
import co.japl.android.finances.services.entities.EmailCreditCardDB
import co.japl.android.finances.services.interfaces.IMapper
import co.japl.android.finances.services.mapping.EmailCreditCardMap
import javax.inject.Inject

class EmailCreditCardImpl @Inject constructor(val dbConnect: SQLiteOpenHelper) : IEmailCreditCardDAO {
    private val mapper: IMapper<EmailCreditCardDTO> = EmailCreditCardMap()
    private val COLUMNS = arrayOf(
        BaseColumns._ID,
        EmailCreditCardDB.EmailCreditCardEntry.COLUMN_SENDER,
        EmailCreditCardDB.EmailCreditCardEntry.COLUMN_SUBJECT_PATTERN,
        EmailCreditCardDB.EmailCreditCardEntry.COLUMN_BODY_PATTERN,
        EmailCreditCardDB.EmailCreditCardEntry.COLUMN_CODE_CREDIT_CARD,
        EmailCreditCardDB.EmailCreditCardEntry.COLUMN_NAME_CREDIT_CARD,
        EmailCreditCardDB.EmailCreditCardEntry.COLUMN_KIND_INTEREST_RATE,
        EmailCreditCardDB.EmailCreditCardEntry.COLUMN_ACTIVE,
        EmailCreditCardDB.EmailCreditCardEntry.COLUMN_CREATE_DATE
    )

    override fun create(dto: EmailCreditCardDTO): Int {
        Log.d(this.javaClass.name, "<<<== create - Start")
        val db = dbConnect.writableDatabase
        val columns = mapper.mapping(dto)
        return db.insert(EmailCreditCardDB.EmailCreditCardEntry.TABLE_NAME, null, columns).toInt().also {
            Log.d(this.javaClass.name, "<<<=== create - response $it End")
        }
    }

    override fun update(dto: EmailCreditCardDTO): Boolean {
        Log.d(this.javaClass.name, "<<<== update - Start")
        val db = dbConnect.writableDatabase
        val columns = mapper.mapping(dto)
        return (db.update(
            EmailCreditCardDB.EmailCreditCardEntry.TABLE_NAME,
            columns,
            "${BaseColumns._ID}=?",
            arrayOf(dto.id.toString())
        ) > 0).also {
            Log.d(this.javaClass.name, "<<<=== update - response $it End")
        }
    }

    override fun getById(id: Int): EmailCreditCardDTO? {
        Log.d(this.javaClass.name, "<<<== getById - Start $id")
        val db = dbConnect.readableDatabase
        db.query(
            EmailCreditCardDB.EmailCreditCardEntry.TABLE_NAME,
            COLUMNS,
            "${BaseColumns._ID}=?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                return mapper.mapping(cursor)
            }
        }
        return null
    }

    override fun getAll(): List<EmailCreditCardDTO> {
        Log.d(this.javaClass.name, "<<<== getAll - Start")
        val list = mutableListOf<EmailCreditCardDTO>()
        val db = dbConnect.readableDatabase
        db.query(
            EmailCreditCardDB.EmailCreditCardEntry.TABLE_NAME,
            COLUMNS,
            null,
            null,
            null,
            null,
            null
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                list.add(mapper.mapping(cursor))
            }
        }
        return list
    }

    override fun delete(id: Int): Boolean {
        Log.d(this.javaClass.name, "<<<== delete - Start $id")
        val db = dbConnect.writableDatabase
        return (db.delete(
            EmailCreditCardDB.EmailCreditCardEntry.TABLE_NAME,
            "${BaseColumns._ID}=?",
            arrayOf(id.toString())
        ) > 0).also {
            Log.d(this.javaClass.name, "<<<=== delete - response $it End")
        }
    }
}
