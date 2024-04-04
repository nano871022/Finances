package co.japl.android.finances.services.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.finances.services.DB.connections.abstracs.DBRestore
import co.japl.android.finances.services.dto.DifferInstallmentDB
import co.japl.android.finances.services.interfaces.IConnectDB
import co.japl.android.finances.services.mapping.DifferInstallmentMap
import co.japl.android.finances.services.queries.DifferInstallmentQuery

class DifferInstallmentConnectDB: DBRestore(), IConnectDB {
    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(this.javaClass.name,"<<<=== DifferInstallmentConnectDB#onCreate - Start")
        db?.execSQL(DifferInstallmentQuery.SQL_CREATE_ENTRIES)
        Log.i(this.javaClass.name,"<<<=== DifferInstallmentConnectDB#onCreate - END")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== DifferInstallmentConnectDB#upgrade - Start")
        if(oldVersion < DifferInstallmentQuery.DATA_BASE_VERSION_MINUS) {
            db?.execSQL(DifferInstallmentQuery.SQL_DELETE_ENTRIES)
            onCreate(db)
        }
        Log.i(this.javaClass.name,"<<<=== DifferInstallmentConnectDB#upgrade - END")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== DifferInstallmentConnectDB#downgrade - Start")
        db?.execSQL(DifferInstallmentQuery.SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.i(this.javaClass.name,"<<<=== DifferInstallmentConnectDB#downgrade - END")
    }

    override fun onRestore(currentDB: SQLiteDatabase?, fromRestoreDB: SQLiteDatabase?) {
        onRestore(currentDB,fromRestoreDB,javaClass.simpleName,DifferInstallmentDB.Entry.TABLE_NAME,DifferInstallmentMap()::restore)
    }
}