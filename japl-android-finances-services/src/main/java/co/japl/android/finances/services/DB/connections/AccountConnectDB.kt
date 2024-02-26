package co.japl.android.finances.services.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.finances.services.interfaces.IConnectDB
import co.japl.android.finances.services.queries.AccountQuery

class AccountConnectDB: IConnectDB {
    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(this.javaClass.name,"<<<=== AccountConnectDB#onCreate - Start")
        db?.execSQL(AccountQuery.SQL_CREATE_ENTRIES)
        Log.i(this.javaClass.name,"<<<=== AccountConnectDB#onCreate - END")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== AccountConnectDB#upgrade - Start")
        if(oldVersion < AccountQuery.DATA_BASE_VERSION_MINUS) {
            db?.execSQL(AccountQuery.SQL_DELETE_ENTRIES)
            onCreate(db)
        }
        Log.i(this.javaClass.name,"<<<=== AccountConnectDB#upgrade - END")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== AccountConnectDB#downgrade - Start")
        db?.execSQL(AccountQuery.SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.i(this.javaClass.name,"<<<=== AccountConnectDB#downgrade - END")
    }
}