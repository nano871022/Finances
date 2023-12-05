package co.japl.android.finances.services.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.finances.services.interfaces.IConnectDB
import co.japl.android.finances.services.queries.AddValueAmortizationQuery

class AddValueAmortizationConnectDB: IConnectDB {
    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(this.javaClass.name,"<<<=== AddValueAmortizationConnectDB#onCreate - Start")
        db?.execSQL(AddValueAmortizationQuery.SQL_CREATE_ENTRIES)
        Log.i(this.javaClass.name,"<<<=== AddValueAmortizationConnectDB#onCreate - END")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== AddValueAmortizationConnectDB#upgrade - Start")
        if(oldVersion < AddValueAmortizationQuery.DATA_BASE_VERSION_MINUS) {
            db?.execSQL(AddValueAmortizationQuery.SQL_DELETE_ENTRIES)
            onCreate(db)
        }
        Log.i(this.javaClass.name,"<<<=== AddValueAmortizationConnectDB#upgrade - END")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== AddValueAmortizationConnectDB#downgrade - Start")
        db?.execSQL(AddValueAmortizationQuery.SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.i(this.javaClass.name,"<<<=== AddValueAmortizationConnectDB#downgrade - END")
    }
}