package co.japl.android.finances.services.mapping

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns
import co.japl.android.finances.services.entities.EmailPaidDB

class EmailPaidMap {

    fun restore(cursor: Cursor): ContentValues {
        return ContentValues().apply {
            put(BaseColumns._ID, cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID)))
            put(EmailPaidDB.EmailPaidEntry.COLUMN_SENDER, cursor.getString(cursor.getColumnIndexOrThrow(EmailPaidDB.EmailPaidEntry.COLUMN_SENDER)))
            put(EmailPaidDB.EmailPaidEntry.COLUMN_SUBJECT_PATTERN, cursor.getString(cursor.getColumnIndexOrThrow(EmailPaidDB.EmailPaidEntry.COLUMN_SUBJECT_PATTERN)))
            put(EmailPaidDB.EmailPaidEntry.COLUMN_BODY_PATTERN, cursor.getString(cursor.getColumnIndexOrThrow(EmailPaidDB.EmailPaidEntry.COLUMN_BODY_PATTERN)))
            put(EmailPaidDB.EmailPaidEntry.COLUMN_CODE_ACCOUNT, cursor.getInt(cursor.getColumnIndexOrThrow(EmailPaidDB.EmailPaidEntry.COLUMN_CODE_ACCOUNT)))
            put(EmailPaidDB.EmailPaidEntry.COLUMN_NAME_ACCOUNT, cursor.getString(cursor.getColumnIndexOrThrow(EmailPaidDB.EmailPaidEntry.COLUMN_NAME_ACCOUNT)))
            put(EmailPaidDB.EmailPaidEntry.COLUMN_ACTIVE, cursor.getInt(cursor.getColumnIndexOrThrow(EmailPaidDB.EmailPaidEntry.COLUMN_ACTIVE)))
            put(EmailPaidDB.EmailPaidEntry.COLUMN_CREATE_DATE, cursor.getString(cursor.getColumnIndexOrThrow(EmailPaidDB.EmailPaidEntry.COLUMN_CREATE_DATE)))
        }
    }
}
