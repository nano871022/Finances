package co.japl.android.finances.services.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.finances.services.DB.connections.abstracs.DBRestore
import co.japl.android.finances.services.entities.EmailCreditCardDB
import co.japl.android.finances.services.interfaces.IConnectDB
import co.japl.android.finances.services.mapping.EmailCreditCardMap
import co.japl.android.finances.services.queries.EmailCreditCardQuery
import java.lang.Exception

class EmailCreditCardConnectDB : DBRestore(), IConnectDB {

    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(this.javaClass.name, "<<<=== EmailCreditCardConnectDB#onCreate - Start")
        db?.execSQL(EmailCreditCardQuery.SQL_CREATE_ENTRIES)
        Log.i(this.javaClass.name, "<<<=== EmailCreditCardConnectDB#onCreate - End")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name, "<<<=== EmailCreditCardConnectDB#onUpgrade - Start $oldVersion - $newVersion")
        // No version logic yet, so just recreate for now or keep it simple
        // In a real app we'd handle versions properly
        if (oldVersion < 4_05_05_082) {
             db?.execSQL(EmailCreditCardQuery.SQL_DELETE_ENTRIES)
             onCreate(db)
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
        onRestore(currentDB, fromRestoreDB, javaClass.simpleName, EmailCreditCardDB.Entry.TABLE_NAME, EmailCreditCardMap()::restore)
    }

    override fun onStats(connectionDB: SQLiteDatabase?): Pair<String, Long> {
        return onStats(connectionDB, EmailCreditCardDB.Entry.TABLE_NAME)
    }

}
