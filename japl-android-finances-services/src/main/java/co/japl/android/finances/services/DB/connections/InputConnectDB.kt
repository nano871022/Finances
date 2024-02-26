package co.japl.android.finances.services.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.finances.services.interfaces.IConnectDB
import co.japl.android.finances.services.queries.InputQuery

class InputConnectDB: IConnectDB {
    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(this.javaClass.name,"<<<=== InputConnectDB#onCreate - Start")
        db?.execSQL(InputQuery.SQL_CREATE_ENTRIES)
        Log.i(this.javaClass.name,"<<<=== InputConnectDB#onCreate - END")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== InputConnectDB#upgrade - Start")
        if(oldVersion < InputQuery.DATA_BASE_VERSION_MINUS) {
            db?.execSQL(InputQuery.SQL_DELETE_ENTRIES)
            onCreate(db)
        }
        Log.i(this.javaClass.name,"<<<=== InputConnectDB#upgrade - END")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== InputConnectDB#downgrade - Start")
        db?.execSQL(InputQuery.SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.i(this.javaClass.name,"<<<=== InputConnectDB#downgrade - END")
    }
}