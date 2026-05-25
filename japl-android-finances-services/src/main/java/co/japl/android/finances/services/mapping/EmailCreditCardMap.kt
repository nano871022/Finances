package co.japl.android.finances.services.mapping

import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns
import co.com.japl.finances.iports.dtos.EmailCreditCardDTO
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.japl.android.finances.services.dto.DifferInstallmentDB
import co.japl.android.finances.services.entities.EmailCreditCardDB
import co.japl.android.finances.services.interfaces.IMapper

class EmailCreditCardMap : IMapper<EmailCreditCardDTO> {

    override fun mapping(dto: EmailCreditCardDTO): ContentValues {
        return ContentValues().apply {
            if (dto.id > 0) {
                put(BaseColumns._ID, dto.id)
            }
            put(EmailCreditCardDB.EmailCreditCardEntry.COLUMN_SENDER, dto.sender)
            put(EmailCreditCardDB.EmailCreditCardEntry.COLUMN_SUBJECT_PATTERN, dto.subjectPattern)
            put(EmailCreditCardDB.EmailCreditCardEntry.COLUMN_BODY_PATTERN, dto.bodyPattern)
            put(EmailCreditCardDB.EmailCreditCardEntry.COLUMN_CODE_CREDIT_CARD, dto.codeCreditCard)
            put(EmailCreditCardDB.EmailCreditCardEntry.COLUMN_NAME_CREDIT_CARD, dto.nameCreditCard)
            put(EmailCreditCardDB.EmailCreditCardEntry.COLUMN_KIND_INTEREST_RATE, dto.kindInterestRateEnum.name)
            put(EmailCreditCardDB.EmailCreditCardEntry.COLUMN_ACTIVE, if (dto.active) 1 else 0)
        }
    }

    override fun mapping(cursor: Cursor): EmailCreditCardDTO {
        return EmailCreditCardDTO(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID)),
            sender = cursor.getString(cursor.getColumnIndexOrThrow(EmailCreditCardDB.EmailCreditCardEntry.COLUMN_SENDER)),
            subjectPattern = cursor.getString(cursor.getColumnIndexOrThrow(EmailCreditCardDB.EmailCreditCardEntry.COLUMN_SUBJECT_PATTERN)),
            bodyPattern = cursor.getString(cursor.getColumnIndexOrThrow(EmailCreditCardDB.EmailCreditCardEntry.COLUMN_BODY_PATTERN)),
            codeCreditCard = cursor.getInt(cursor.getColumnIndexOrThrow(EmailCreditCardDB.EmailCreditCardEntry.COLUMN_CODE_CREDIT_CARD)),
            nameCreditCard = cursor.getString(cursor.getColumnIndexOrThrow(EmailCreditCardDB.EmailCreditCardEntry.COLUMN_NAME_CREDIT_CARD)),
            kindInterestRateEnum = KindInterestRateEnum.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(EmailCreditCardDB.EmailCreditCardEntry.COLUMN_KIND_INTEREST_RATE))),
            active = cursor.getInt(cursor.getColumnIndexOrThrow(EmailCreditCardDB.EmailCreditCardEntry.COLUMN_ACTIVE)) == 1
        )
    }

    fun restore(crsor:Cursor):ContentValues {
        return ContentValues().apply {
            put(BaseColumns._ID, crsor.getLong(0))
            put(EmailCreditCardDB.EmailCreditCardEntry.COLUMN_SENDER, crsor.getString(1))
            put(EmailCreditCardDB.EmailCreditCardEntry.COLUMN_SUBJECT_PATTERN, crsor.getString(2))
            put(EmailCreditCardDB.EmailCreditCardEntry.COLUMN_BODY_PATTERN, crsor.getString(3))
            put(EmailCreditCardDB.EmailCreditCardEntry.COLUMN_CODE_CREDIT_CARD, crsor.getInt(4))
            put(EmailCreditCardDB.EmailCreditCardEntry.COLUMN_NAME_CREDIT_CARD, crsor.getString(5))
            put(EmailCreditCardDB.EmailCreditCardEntry.COLUMN_KIND_INTEREST_RATE, crsor.getString(6))
            put(EmailCreditCardDB.EmailCreditCardEntry.COLUMN_ACTIVE, crsor.getInt(7))
        }
    }
}
