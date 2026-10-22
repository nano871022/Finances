package co.japl.android.finances.services.mapping

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import android.provider.BaseColumns
import androidx.annotation.RequiresApi
import co.japl.android.finances.services.dto.TaxDB
import co.japl.android.finances.services.dto.TaxDTO
import co.japl.android.finances.services.interfaces.IMapper
import co.japl.android.finances.services.utils.DateUtils

class TaxMap : IMapper<TaxDTO>{
    @RequiresApi(Build.VERSION_CODES.O)
    override fun mapping(dto: TaxDTO ): ContentValues {
        return ContentValues().apply {
            put(TaxDB.TaxEntry.COLUMN_TAX, dto.value)
            put(TaxDB.TaxEntry.COLUMN_MONTH, dto.month)
            put(TaxDB.TaxEntry.COLUMN_YEAR, dto.year.toString())
            put(TaxDB.TaxEntry.COLUMN_COD_CREDIT_CARD, dto.codCreditCard.toString())
            put(TaxDB.TaxEntry.COLUMN_status, dto.status)
            put(TaxDB.TaxEntry.COLUMN_CREATE_DATE, DateUtils.localDateTimeToString(dto.create))
            put(TaxDB.TaxEntry.COLUMN_KIND, dto.kind)
            put(TaxDB.TaxEntry.COLUMN_PERIOD, dto.period)
            put(TaxDB.TaxEntry.COLUMN_KIND_OF_TAX, dto.kindOfTax)
            put(TaxDB.TaxEntry.COLUMN_CASH_ADVANCE_VALUE, dto.cashAdvanceValue)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun mapping(cursor: Cursor): TaxDTO {
        val id = cursor.getString(cursor.getColumnIndexOrThrow(BaseColumns._ID)).toInt()
        val tax = cursor.getDouble(cursor.getColumnIndexOrThrow(TaxDB.TaxEntry.COLUMN_TAX))
        val month = cursor.getShort(cursor.getColumnIndexOrThrow(TaxDB.TaxEntry.COLUMN_MONTH))
        val year = cursor.getInt(cursor.getColumnIndexOrThrow(TaxDB.TaxEntry.COLUMN_YEAR))
        val status = cursor.getShort(cursor.getColumnIndexOrThrow(TaxDB.TaxEntry.COLUMN_status))
        val codCreditCard = cursor.getInt(cursor.getColumnIndexOrThrow(TaxDB.TaxEntry.COLUMN_COD_CREDIT_CARD))
        val createDate = DateUtils.toLocalDateTime(cursor.getString(cursor.getColumnIndexOrThrow(TaxDB.TaxEntry.COLUMN_CREATE_DATE)))
        val kind = cursor.getShort(cursor.getColumnIndexOrThrow(TaxDB.TaxEntry.COLUMN_KIND))
        val period = cursor.getShort(cursor.getColumnIndexOrThrow(TaxDB.TaxEntry.COLUMN_PERIOD))
        val kindOfTax = cursor.getString(cursor.getColumnIndexOrThrow(TaxDB.TaxEntry.COLUMN_KIND_OF_TAX))
        val cashAdvanceIdx = cursor.getColumnIndex(TaxDB.TaxEntry.COLUMN_CASH_ADVANCE_VALUE)
        val cashAdvanceValue = if (cashAdvanceIdx != -1 && !cursor.isNull(cashAdvanceIdx)) cursor.getDouble(cashAdvanceIdx) else null

        return TaxDTO(
            id,
            month,
            year,
            status,
            codCreditCard,
            createDate,
            tax,
            kind,
            period,
            kindOfTax,
            cashAdvanceValue
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun restore(crsor: Cursor): ContentValues {
        return ContentValues().apply {
            put(BaseColumns._ID, crsor.getLong(crsor.getColumnIndexOrThrow(BaseColumns._ID)))
            put(TaxDB.TaxEntry.COLUMN_TAX, crsor.getDouble(crsor.getColumnIndexOrThrow(TaxDB.TaxEntry.COLUMN_TAX)))
            put(TaxDB.TaxEntry.COLUMN_MONTH, crsor.getShort(crsor.getColumnIndexOrThrow(TaxDB.TaxEntry.COLUMN_MONTH)))
            put(TaxDB.TaxEntry.COLUMN_YEAR, crsor.getInt(crsor.getColumnIndexOrThrow(TaxDB.TaxEntry.COLUMN_YEAR)))
            put(TaxDB.TaxEntry.COLUMN_COD_CREDIT_CARD, crsor.getInt(crsor.getColumnIndexOrThrow(TaxDB.TaxEntry.COLUMN_COD_CREDIT_CARD)))
            put(TaxDB.TaxEntry.COLUMN_status, crsor.getShort(crsor.getColumnIndexOrThrow(TaxDB.TaxEntry.COLUMN_status)))
            put(TaxDB.TaxEntry.COLUMN_CREATE_DATE, crsor.getString(crsor.getColumnIndexOrThrow(TaxDB.TaxEntry.COLUMN_CREATE_DATE)))
            put(TaxDB.TaxEntry.COLUMN_KIND, crsor.getShort(crsor.getColumnIndexOrThrow(TaxDB.TaxEntry.COLUMN_KIND)))
            put(TaxDB.TaxEntry.COLUMN_PERIOD, crsor.getShort(crsor.getColumnIndexOrThrow(TaxDB.TaxEntry.COLUMN_PERIOD)))
            put(TaxDB.TaxEntry.COLUMN_KIND_OF_TAX, crsor.getString(crsor.getColumnIndexOrThrow(TaxDB.TaxEntry.COLUMN_KIND_OF_TAX)))
            val idx = crsor.getColumnIndex(TaxDB.TaxEntry.COLUMN_CASH_ADVANCE_VALUE)
            if (idx != -1 && !crsor.isNull(idx)) {
                put(TaxDB.TaxEntry.COLUMN_CASH_ADVANCE_VALUE, crsor.getDouble(idx))
            }
        }
    }
}
