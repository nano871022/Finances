package co.japl.android.myapplication.finanzas.bussiness.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.myapplication.bussiness.interfaces.IConnectDB
import co.japl.android.myapplication.finanzas.bussiness.queries.DifferInstallmentQuery

class DifferInstallmentConnectDB: IConnectDB {
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
}