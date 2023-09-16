package co.japl.android.myapplication.finanzas.bussiness.queries

import android.provider.BaseColumns
import co.japl.android.myapplication.finanzas.bussiness.DTO.AccountDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.CreditDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.InputDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.PaidDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.TagDB

object TagsQuery {
    const val SQL_CREATE_ENTRIES = """
        CREATE TABLE ${TagDB.Entry.TABLE_NAME}(
            ${BaseColumns._ID} INTEGER PRIMARY KEY,
            ${TagDB.Entry.COLUMN_NAME} TEXT,
            ${TagDB.Entry.COLUMN_DATE_CREATE} DATE,
            ${TagDB.Entry.COLUMN_ACTIVE} NUMBER
        )
    """
    const val DATA_BASE_VERSION_MINUS = 4_05_03_040
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${TagDB.Entry.TABLE_NAME}"
    val SQL_ALTER = emptyMap<String,String>()
}