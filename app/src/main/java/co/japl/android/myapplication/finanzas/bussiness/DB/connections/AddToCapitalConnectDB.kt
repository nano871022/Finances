package co.japl.android.myapplication.finanzas.bussiness.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.myapplication.bussiness.interfaces.IConnectDB
import co.japl.android.myapplication.finanzas.bussiness.queries.AddToCapitalQuery
import co.japl.android.myapplication.finanzas.bussiness.queries.CreditQuery
import co.japl.android.myapplication.utils.DatabaseConstants

class AddToCapitalConnectDB: IConnectDB {
    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(this.javaClass.name,"<<<=== AddToCapitalConnectDB#onCreate - Start")
        db?.execSQL(AddToCapitalQuery.SQL_CREATE_ENTRIES)
        Log.i(this.javaClass.name,"<<<=== AddToCapitalConnectDB#onCreate - END")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== AddToCapitalConnectDB#upgrade - Start")
        if(oldVersion < AddToCapitalQuery.DATA_BASE_VERSION_MINUS_SETTING) {
            db?.execSQL(AddToCapitalQuery.SQL_DELETE_ENTRIES)
            onCreate(db)
        }
        Log.i(this.javaClass.name,"<<<=== AddToCapitalConnectDB#upgrade - END")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== AddToCapitalConnectDB#downgrade - Start")
        db?.execSQL(AddToCapitalQuery.SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.i(this.javaClass.name,"<<<=== AddToCapitalConnectDB#downgrade - END")
    }
}