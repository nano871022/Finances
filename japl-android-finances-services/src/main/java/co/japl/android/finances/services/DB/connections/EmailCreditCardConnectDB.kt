package co.japl.android.finances.services.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.finances.services.DB.connections.abstracs.DBRestore
import co.japl.android.finances.services.entities.EmailCreditCardDB
import co.japl.android.finances.services.interfaces.IConnectDB
import co.japl.android.finances.services.mapping.EmailCreditCardMap
import co.japl.android.finances.services.queries.EmailCreditCardQuery
import co.japl.android.finances.services.utils.DatabaseConstants

class EmailCreditCardConnectDB : DBRestore(), IConnectDB {

    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(this.javaClass.name, "<<<=== EmailCreditCardConnectDB#onCreate - Start")
        db?.execSQL(EmailCreditCardQuery.SQL_CREATE_ENTRIES)
        Log.i(this.javaClass.name, "<<<=== EmailCreditCardConnectDB#onCreate - End")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name, "<<<=== EmailCreditCardConnectDB#onUpgrade - Start $oldVersion - $newVersion")
        if (oldVersion < EmailCreditCardQuery.DATA_BASE_VERSION_MINUS) {
            db?.execSQL(EmailCreditCardQuery.SQL_DELETE_ENTRIES)
            onCreate(db)
        } else {
            // Future migrations can be added here
        }
        Log.i(this.javaClass.name, "<<<=== EmailCreditCardConnectDB#onUpgrade - End")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name, "<<<=== EmailCreditCardConnectDB#onDowngrade - Start $oldVersion - $newVersion")
        db?.execSQL(EmailCreditCardQuery.SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.i(this.javaClass.name, "<<<=== EmailCreditCardConnectDB#onDowngrade - End")
    }

    override fun onRestore(currentDB: SQLiteDatabase?, fromRestoreDB: SQLiteDatabase?) {
        onRestore(currentDB,fromRestoreDB, javaClass.simpleName,EmailCreditCardDB.EmailCreditCardEntry.TABLE_NAME,EmailCreditCardMap()::restore)
    }

    override fun onStats(connectionDB: SQLiteDatabase?): Pair<String, Long> {
        return onStats(connectionDB, EmailCreditCardDB.EmailCreditCardEntry.TABLE_NAME)
    }
}
