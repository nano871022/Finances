package co.japl.android.myapplication.finanzas.bussiness.queries

import android.provider.BaseColumns
import co.japl.android.myapplication.finanzas.bussiness.DTO.DifferInstallmentDB

object DifferInstallmentQuery {
    const val SQL_CREATE_ENTRIES = """
        CREATE TABLE ${DifferInstallmentDB.Entry.TABLE_NAME}(
            ${BaseColumns._ID} INTEGER PRIMARY KEY,
            ${DifferInstallmentDB.Entry.COLUMN_DATE_CREATE} DATE,
            ${DifferInstallmentDB.Entry.COLUMN_CODE_QUOTE} INTEGER,
            ${DifferInstallmentDB.Entry.COLUMN_PENDING_VALUE_PAYABLE} NUMBER,
            ${DifferInstallmentDB.Entry.COLUMN_ORIGIN_VALUE} NUMBER,
            ${DifferInstallmentDB.Entry.COLUMN_NEW_INSTALLMENT} SHORT,
            ${DifferInstallmentDB.Entry.COLUMN_OLD_INSTALLMENT} SHORT
        )
    """
    const val DATA_BASE_VERSION_MINUS = 4_04_09_033
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${DifferInstallmentDB.Entry.TABLE_NAME}"
}