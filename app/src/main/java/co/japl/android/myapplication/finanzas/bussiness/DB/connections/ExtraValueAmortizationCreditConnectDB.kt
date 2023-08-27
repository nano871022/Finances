package co.japl.android.myapplication.finanzas.bussiness.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.myapplication.bussiness.interfaces.IConnectDB
import co.japl.android.myapplication.finanzas.bussiness.queries.ExtraValueAmortizationCreditQuery
import co.japl.android.myapplication.finanzas.bussiness.queries.CreditQuery
import co.japl.android.myapplication.utils.DatabaseConstants

class ExtraValueAmortizationCreditConnectDB: IConnectDB {
    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(this.javaClass.name,"<<<=== ExtraValueAmortizationCreditConnectDB#onCreate - Start")
        db?.execSQL(ExtraValueAmortizationCreditQuery.SQL_CREATE_ENTRIES)
        Log.i(this.javaClass.name,"<<<=== ExtraValueAmortizationCreditConnectDB#onCreate - END")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== ExtraValueAmortizationCreditConnectDB#upgrade - Start")
        if(oldVersion < ExtraValueAmortizationCreditQuery.DATA_BASE_VERSION_MINUS) {
            db?.execSQL(ExtraValueAmortizationCreditQuery.SQL_DELETE_ENTRIES)
            onCreate(db)
        }
        Log.i(this.javaClass.name,"<<<=== ExtraValueAmortizationCreditConnectDB#upgrade - END")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== ExtraValueAmortizationCreditConnectDB#downgrade - Start")
        db?.execSQL(ExtraValueAmortizationCreditQuery.SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.i(this.javaClass.name,"<<<=== ExtraValueAmortizationCreditConnectDB#downgrade - END")
    }
}