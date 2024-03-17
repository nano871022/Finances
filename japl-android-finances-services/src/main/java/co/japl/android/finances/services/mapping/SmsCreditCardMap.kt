package co.japl.android.finances.services.mapping

import android.content.ContentValues
import android.database.Cursor
import co.com.japl.finances.iports.enums.KindInterestRateEnum
import co.japl.android.finances.services.entities.SmsCreditCard
import co.japl.android.finances.services.entities.SmsCreditCardDB
import co.japl.android.finances.services.utils.DateUtils

class SmsCreditCardMap {

    fun mapping(cursor:Cursor):SmsCreditCard{
        return SmsCreditCard(
            id= cursor.getInt(0),
            codeCreditCard = cursor.getInt(1),
            nameCreditCard = "",
            phoneNumber = cursor.getString(2),
            pattern = cursor.getString(3),
            kindInterestRateEnum = KindInterestRateEnum.findByOrdinal(cursor.getShort(4)),
            active = cursor.getInt(5) == 1,
            create = DateUtils.toLocalDateTime(cursor.getString(6))
        )
    }

    fun mapping(dto:SmsCreditCard):ContentValues{
        return ContentValues().apply {
            put(SmsCreditCardDB.Entry.COLUMN_CODE_CREDIT_CARD,dto.codeCreditCard)
            put(SmsCreditCardDB.Entry.COLUMN_PHONE_NUMBER,dto.phoneNumber)
            put(SmsCreditCardDB.Entry.COLUMN_PATTERN,dto.pattern)
            put(SmsCreditCardDB.Entry.COLUMN_KIND_INTEREST_RATE,dto.kindInterestRateEnum.getCode())
            put(SmsCreditCardDB.Entry.COLUMN_ACTIVE,if(dto.active) 1 else 0)
            put(SmsCreditCardDB.Entry.COLUMN_CREATE_DATE,DateUtils.localDateTimeToStringDB(dto.create))
        }
    }
}