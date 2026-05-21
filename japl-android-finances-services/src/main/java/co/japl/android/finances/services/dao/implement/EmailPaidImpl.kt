package co.japl.android.finances.services.dao.implement

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns
import co.com.japl.finances.iports.dtos.EmailPaidDTO
import co.japl.android.finances.services.DB.connections.ConnectDB
import co.japl.android.finances.services.dao.interfaces.IEmailPaidDAO
import co.japl.android.finances.services.entities.EmailPaidDB
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class EmailPaidImpl @Inject constructor(private val connect: ConnectDB) : IEmailPaidDAO {

    private val columns = arrayOf(
        BaseColumns._ID,
        EmailPaidDB.EmailPaidEntry.COLUMN_SENDER,
        EmailPaidDB.EmailPaidEntry.COLUMN_SUBJECT_PATTERN,
        EmailPaidDB.EmailPaidEntry.COLUMN_BODY_PATTERN,
        EmailPaidDB.EmailPaidEntry.COLUMN_CODE_ACCOUNT,
        EmailPaidDB.EmailPaidEntry.COLUMN_NAME_ACCOUNT,
        EmailPaidDB.EmailPaidEntry.COLUMN_ACTIVE,
        EmailPaidDB.EmailPaidEntry.COLUMN_CREATE_DATE
    )

    override fun create(dto: EmailPaidDTO): Int {
        val db = connect.writableDatabase
        val values = ContentValues().apply {
            put(EmailPaidDB.EmailPaidEntry.COLUMN_SENDER, dto.sender)
            put(EmailPaidDB.EmailPaidEntry.COLUMN_SUBJECT_PATTERN, dto.subjectPattern)
            put(EmailPaidDB.EmailPaidEntry.COLUMN_BODY_PATTERN, dto.bodyPattern)
            put(EmailPaidDB.EmailPaidEntry.COLUMN_CODE_ACCOUNT, dto.codeAccount)
            put(EmailPaidDB.EmailPaidEntry.COLUMN_NAME_ACCOUNT, dto.nameAccount)
            put(EmailPaidDB.EmailPaidEntry.COLUMN_ACTIVE, if (dto.active) 1 else 0)
            put(EmailPaidDB.EmailPaidEntry.COLUMN_CREATE_DATE, LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
        }
        return db.insert(EmailPaidDB.EmailPaidEntry.TABLE_NAME, null, values).toInt()
    }

    override fun update(dto: EmailPaidDTO): Boolean {
        val db = connect.writableDatabase
        val values = ContentValues().apply {
            put(EmailPaidDB.EmailPaidEntry.COLUMN_SENDER, dto.sender)
            put(EmailPaidDB.EmailPaidEntry.COLUMN_SUBJECT_PATTERN, dto.subjectPattern)
            put(EmailPaidDB.EmailPaidEntry.COLUMN_BODY_PATTERN, dto.bodyPattern)
            put(EmailPaidDB.EmailPaidEntry.COLUMN_CODE_ACCOUNT, dto.codeAccount)
            put(EmailPaidDB.EmailPaidEntry.COLUMN_NAME_ACCOUNT, dto.nameAccount)
            put(EmailPaidDB.EmailPaidEntry.COLUMN_ACTIVE, if (dto.active) 1 else 0)
        }
        return db.update(EmailPaidDB.EmailPaidEntry.TABLE_NAME, values, "${BaseColumns._ID} = ?", arrayOf(dto.id.toString())) > 0
    }

    override fun getById(id: Int): EmailPaidDTO? {
        val db = connect.readableDatabase
        val cursor = db.query(EmailPaidDB.EmailPaidEntry.TABLE_NAME, columns, "${BaseColumns._ID} = ?", arrayOf(id.toString()), null, null, null)
        return if (cursor.moveToFirst()) {
            map(cursor)
        } else {
            null
        }.also { cursor.close() }
    }

    override fun getAll(): List<EmailPaidDTO> {
        val db = connect.readableDatabase
        val cursor = db.query(EmailPaidDB.EmailPaidEntry.TABLE_NAME, columns, null, null, null, null, null)
        val list = mutableListOf<EmailPaidDTO>()
        while (cursor.moveToNext()) {
            list.add(map(cursor))
        }
        cursor.close()
        return list
    }

    override fun delete(id: Int): Boolean {
        val db = connect.writableDatabase
        return db.delete(EmailPaidDB.EmailPaidEntry.TABLE_NAME, "${BaseColumns._ID} = ?", arrayOf(id.toString())) > 0
    }

    private fun map(cursor: Cursor): EmailPaidDTO {
        return EmailPaidDTO(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID)),
            sender = cursor.getString(cursor.getColumnIndexOrThrow(EmailPaidDB.EmailPaidEntry.COLUMN_SENDER)),
            subjectPattern = cursor.getString(cursor.getColumnIndexOrThrow(EmailPaidDB.EmailPaidEntry.COLUMN_SUBJECT_PATTERN)),
            bodyPattern = cursor.getString(cursor.getColumnIndexOrThrow(EmailPaidDB.EmailPaidEntry.COLUMN_BODY_PATTERN)),
            codeAccount = cursor.getInt(cursor.getColumnIndexOrThrow(EmailPaidDB.EmailPaidEntry.COLUMN_CODE_ACCOUNT)),
            nameAccount = cursor.getString(cursor.getColumnIndexOrThrow(EmailPaidDB.EmailPaidEntry.COLUMN_NAME_ACCOUNT)),
            active = cursor.getInt(cursor.getColumnIndexOrThrow(EmailPaidDB.EmailPaidEntry.COLUMN_ACTIVE)) == 1
        )
    }
}
