package co.japl.android.finances.services.dao.implement

import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.japl.android.finances.services.dao.interfaces.IEmailCreditCardDAO
import co.japl.android.finances.services.entities.EmailCreditCard
import co.japl.android.finances.services.entities.EmailCreditCardDB
import co.japl.android.finances.services.mapping.EmailCreditCardMap
import java.util.Optional
import javax.inject.Inject

class EmailCreditCardImpl @Inject constructor(override var dbConnect: SQLiteOpenHelper) : IEmailCreditCardDAO {

    val COLUMNS = arrayOf(
        BaseColumns._ID,
        EmailCreditCardDB.Entry.COLUMN_CODE_CREDIT_CARD,
        EmailCreditCardDB.Entry.COLUMN_SENDER,
        EmailCreditCardDB.Entry.COLUMN_SUBJECT_PATTERN,
        EmailCreditCardDB.Entry.COLUMN_BODY_PATTERN,
        EmailCreditCardDB.Entry.COLUMN_KIND_INTEREST_RATE,
        EmailCreditCardDB.Entry.COLUMN_CREATE_DATE,
        EmailCreditCardDB.Entry.COLUMN_ACTIVE
    )

    override fun getByCreditCardAndKindInterest(
        codeCreditCard: Int,
        kind: KindInterestRateEnum
    ): List<EmailCreditCard> {
        val list = mutableListOf<EmailCreditCard>()
        val db = dbConnect.readableDatabase
        db.query(
            EmailCreditCardDB.Entry.TABLE_NAME,
            COLUMNS,
            "${EmailCreditCardDB.Entry.COLUMN_CODE_CREDIT_CARD}=? AND ${EmailCreditCardDB.Entry.COLUMN_KIND_INTEREST_RATE}=? AND ${EmailCreditCardDB.Entry.COLUMN_ACTIVE}=1",
            arrayOf(codeCreditCard.toString(), kind.getCode().toString()),
            null,
            null,
            null
        )?.use { cursor ->
            with(cursor) {
                while (moveToNext()) {
                    list.add(EmailCreditCardMap().mapping(cursor))
                }
            }
        }
        return list
    }

    override fun save(dto: EmailCreditCard): Long {
        val db = dbConnect.readableDatabase
        val content = EmailCreditCardMap().mapping(dto)
        return dto.takeIf { it.id != null && it.id!! > 0 }?.let {
            db.update(EmailCreditCardDB.Entry.TABLE_NAME, content, "${BaseColumns._ID} = ?", arrayOf(it.id.toString())).toLong()
        } ?: db.insert(EmailCreditCardDB.Entry.TABLE_NAME, null, content)
    }

    override fun getAll(): List<EmailCreditCard> {
        val list = mutableListOf<EmailCreditCard>()
        val mapper = EmailCreditCardMap()
        val db = dbConnect.readableDatabase
        db.query(EmailCreditCardDB.Entry.TABLE_NAME, COLUMNS, null, null, null, null, null)
            ?.use { cursor ->
                with(cursor) {
                    while (moveToNext()) {
                        mapper.mapping(cursor).let(list::add)
                    }
                }
            }
        return list
    }

    override fun delete(id: Int): Boolean {
        require(id > 0) { "Id must be > 0" }
        val db = dbConnect.readableDatabase
        return db.delete(EmailCreditCardDB.Entry.TABLE_NAME, "${BaseColumns._ID} = ?", arrayOf(id.toString())) > 0
    }

    override fun get(id: Int): Optional<EmailCreditCard> {
        require(id > 0) { "Id must be > 0" }
        val mapper = EmailCreditCardMap()
        val db = dbConnect.readableDatabase
        db.query(
            EmailCreditCardDB.Entry.TABLE_NAME, COLUMNS, "${BaseColumns._ID} = ?",
            arrayOf(id.toString()), null, null, null
        )?.use { cursor ->
            with(cursor) {
                while (moveToNext()) {
                    mapper.mapping(cursor).let { return Optional.of(it) }
                }
            }
        }
        return Optional.empty()
    }

    override fun get(values: EmailCreditCard): List<EmailCreditCard> {
        val list = mutableListOf<EmailCreditCard>()
        val mapper = EmailCreditCardMap()
        val db = dbConnect.readableDatabase
        db.query(
            EmailCreditCardDB.Entry.TABLE_NAME, COLUMNS,
            "${EmailCreditCardDB.Entry.COLUMN_CODE_CREDIT_CARD} =?",
            arrayOf(values.codeCreditCard.toString()), null, null, null
        )
            ?.use { cursor ->
                with(cursor) {
                    while (moveToNext()) {
                        mapper.mapping(cursor).let(list::add)
                    }
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
