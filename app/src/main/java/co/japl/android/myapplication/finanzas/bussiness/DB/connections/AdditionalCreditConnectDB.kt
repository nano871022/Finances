package co.japl.android.myapplication.finanzas.bussiness.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.myapplication.bussiness.interfaces.IConnectDB
import co.japl.android.myapplication.finanzas.bussiness.queries.AddToCapitalQuery
import co.japl.android.myapplication.finanzas.bussiness.queries.AdditionalCreditQuery
import co.japl.android.myapplication.finanzas.bussiness.queries.CreditQuery
import co.japl.android.myapplication.utils.DatabaseConstants

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