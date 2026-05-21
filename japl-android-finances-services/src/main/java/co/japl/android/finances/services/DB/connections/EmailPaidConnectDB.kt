package co.japl.android.finances.services.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.finances.services.DB.connections.abstracs.DBRestore
import co.japl.android.finances.services.entities.EmailPaidDB
import co.japl.android.finances.services.interfaces.IConnectDB
import co.japl.android.finances.services.mapping.EmailPaidMap
import co.japl.android.finances.services.queries.EmailPaidQuery

class EmailPaidConnectDB : DBRestore(), IConnectDB {

    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(this.javaClass.name, "<<<=== EmailPaidConnectDB#onCreate - Start")
        db?.execSQL(EmailPaidQuery.SQL_CREATE_ENTRIES)
        Log.i(this.javaClass.name, "<<<=== EmailPaidConnectDB#onCreate - End")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name, "<<<=== EmailPaidConnectDB#onUpgrade - Start $oldVersion - $newVersion")
        if (oldVersion < 4_08_01_169) {
            db?.execSQL(EmailPaidQuery.SQL_DELETE_ENTRIES)
            onCreate(db)
        }
        Log.i(this.javaClass.name, "<<<=== EmailPaidConnectDB#onUpgrade - End")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name, "<<<=== EmailPaidConnectDB#onDowngrade - Start $oldVersion - $newVersion")
        db?.execSQL(EmailPaidQuery.SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.i(this.javaClass.name, "<<<=== EmailPaidConnectDB#onDowngrade - End")
    }

    override fun onRestore(currentDB: SQLiteDatabase?, fromRestoreDB: SQLiteDatabase?) {
        onRestore(currentDB, fromRestoreDB, javaClass.simpleName, EmailPaidDB.EmailPaidEntry.TABLE_NAME, EmailPaidMap()::restore)
    }

    override fun onStats(connectionDB: SQLiteDatabase?): Pair<String, Long> {
        return onStats(connectionDB, EmailPaidDB.EmailPaidEntry.TABLE_NAME)
    }
}
