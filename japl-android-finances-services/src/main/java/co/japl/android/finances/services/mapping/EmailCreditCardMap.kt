package co.japl.android.finances.services.mapping

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.japl.android.finances.services.entities.EmailCreditCard
import co.japl.android.finances.services.entities.EmailCreditCardDB
import co.japl.android.finances.services.utils.DateUtils

class EmailCreditCardMap {

    fun mapping(cursor: Cursor): EmailCreditCard {
        return EmailCreditCard(
            id = cursor.getInt(0),
            codeCreditCard = cursor.getInt(1),
            nameCreditCard = "",
            sender = cursor.getString(2),
            subjectPattern = cursor.getString(3),
            bodyPattern = cursor.getString(4),
            kindInterestRateEnum = KindInterestRateEnum.findByOrdinal(cursor.getShort(5)),
            create = DateUtils.toLocalDateTime(cursor.getString(6)),
            active = cursor.getInt(7) == 1
        )
    }

    fun mapping(dto: EmailCreditCard): ContentValues {
        return ContentValues().apply {
            put(EmailCreditCardDB.Entry.COLUMN_CODE_CREDIT_CARD, dto.codeCreditCard)
            put(EmailCreditCardDB.Entry.COLUMN_SENDER, dto.sender)
            put(EmailCreditCardDB.Entry.COLUMN_SUBJECT_PATTERN, dto.subjectPattern)
            put(EmailCreditCardDB.Entry.COLUMN_BODY_PATTERN, dto.bodyPattern)
            put(EmailCreditCardDB.Entry.COLUMN_KIND_INTEREST_RATE, dto.kindInterestRateEnum!!.getCode())
            put(EmailCreditCardDB.Entry.COLUMN_ACTIVE, if (dto.active!!) 1 else 0)
            put(EmailCreditCardDB.Entry.COLUMN_CREATE_DATE, DateUtils.localDateTimeToStringDB(dto.create!!))
        }
    }

    fun restore(crsor: Cursor): ContentValues {
        return ContentValues().apply {
            put(BaseColumns._ID, crsor.getLong(0))
            put(EmailCreditCardDB.Entry.COLUMN_CODE_CREDIT_CARD, crsor.getInt(1))
            put(EmailCreditCardDB.Entry.COLUMN_SENDER, crsor.getString(2))
            put(EmailCreditCardDB.Entry.COLUMN_SUBJECT_PATTERN, crsor.getString(3))
            put(EmailCreditCardDB.Entry.COLUMN_BODY_PATTERN, crsor.getString(4))
            put(EmailCreditCardDB.Entry.COLUMN_KIND_INTEREST_RATE, crsor.getInt(5))
            put(EmailCreditCardDB.Entry.COLUMN_CREATE_DATE, crsor.getString(6))
            put(EmailCreditCardDB.Entry.COLUMN_ACTIVE, crsor.getInt(7))
        }
    }
}
