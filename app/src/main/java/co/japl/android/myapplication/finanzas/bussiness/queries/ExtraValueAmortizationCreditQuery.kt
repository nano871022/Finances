package co.japl.android.myapplication.finanzas.bussiness.queries

import android.provider.BaseColumns
import co.japl.android.myapplication.finanzas.bussiness.DTO.AccountDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.AddAmortizationDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.CreditDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.ExtraValueAmortizationCreditDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.InputDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidDB

object ExtraValueAmortizationCreditQuery {
    const val SQL_CREATE_ENTRIES = """
        CREATE TABLE ${ExtraValueAmortizationCreditDB.Entry.TABLE_NAME}(
            ${BaseColumns._ID} INTEGER PRIMARY KEY,
            ${ExtraValueAmortizationCreditDB.Entry.COLUMN_DATE_CREATE} DATE,
            ${ExtraValueAmortizationCreditDB.Entry.COLUMN_CODE} INTEGER,
            ${ExtraValueAmortizationCreditDB.Entry.COLUMN_NBR_QUOTE} NUMBER,
            ${ExtraValueAmortizationCreditDB.Entry.COLUMN_VALUE} NUMBER
        )
    """
    const val DATA_BASE_VERSION_MINUS = 4_05_01_037
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${ExtraValueAmortizationCreditDB.Entry.TABLE_NAME}"
}