package co.japl.android.finances.services.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.finances.services.interfaces.IConnectDB
import co.japl.android.finances.services.queries.AdditionalCreditQuery

class AdditionalCreditConnectDB: IConnectDB {
    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(this.javaClass.name,"<<<=== AdditionalCreditConnectDB#onCreate - Start")
        db?.execSQL(AdditionalCreditQuery.SQL_CREATE_ENTRIES)
        Log.i(this.javaClass.name,"<<<=== AdditionalCreditConnectDB#onCreate - END")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== AdditionalCreditConnectDB#upgrade - Start")
        if(oldVersion < AdditionalCreditQuery.DATA_BASE_VERSION_MINUS_SETTING) {
            db?.execSQL(AdditionalCreditQuery.SQL_DELETE_ENTRIES)
            onCreate(db)
        }
        Log.i(this.javaClass.name,"<<<=== AdditionalCreditConnectDB#upgrade - END")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== AdditionalCreditConnectDB#downgrade - Start")
        db?.execSQL(AdditionalCreditQuery.SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.i(this.javaClass.name,"<<<=== AdditionalCreditConnectDB#downgrade - END")
    }
}