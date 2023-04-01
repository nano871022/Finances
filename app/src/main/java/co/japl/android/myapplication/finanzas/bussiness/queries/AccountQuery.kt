package co.japl.android.myapplication.finanzas.bussiness.queries

import android.provider.BaseColumns
import co.japl.android.myapplication.finanzas.bussiness.DTO.AccountDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.CreditDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.InputDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidDB

object AccountQuery {
    const val SQL_CREATE_ENTRIES = """
        CREATE TABLE ${AccountDB.Entry.TABLE_NAME}(
            ${BaseColumns._ID} INTEGER PRIMARY KEY,
            ${AccountDB.Entry.COLUMN_DATE_CREATE} DATE,
            ${AccountDB.Entry.COLUMN_NAME} TEXT,
            ${AccountDB.Entry.COLUMN_ACTIVE} NUMBER,
        )
    """
    const val DATA_BASE_VERSION_MINUS = 28
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${AccountDB.Entry.TABLE_NAME}"
}