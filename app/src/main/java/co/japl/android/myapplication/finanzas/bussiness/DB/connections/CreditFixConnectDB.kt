package co.japl.android.myapplication.finanzas.bussiness.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.myapplication.bussiness.interfaces.IConnectDB
import co.japl.android.myapplication.finanzas.bussiness.queries.CreditQuery
import co.japl.android.myapplication.utils.DatabaseConstants

class CreditFixConnectDB: IConnectDB {
    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(this.javaClass.name,"<<<=== CreditFixConnectDB#onCreate - Start")
        db?.execSQL(CreditQuery.SQL_CREATE_ENTRIES)
        Log.i(this.javaClass.name,"<<<=== CreditFixConnectDB#onCreate - END")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== CreditFixConnectDB#upgrade - Start")
        if(oldVersion < CreditQuery.DATA_BASE_VERSION_MINUS_SETTING) {
            db?.execSQL(CreditQuery.SQL_DELETE_ENTRIES)
            onCreate(db)
        }
        Log.i(this.javaClass.name,"<<<=== CreditFixConnectDB#upgrade - END")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== CreditFixConnectDB#downgrade - Start")
        db?.execSQL(CreditQuery.SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.i(this.javaClass.name,"<<<=== CreditFixConnectDB#downgrade - END")
    }
}