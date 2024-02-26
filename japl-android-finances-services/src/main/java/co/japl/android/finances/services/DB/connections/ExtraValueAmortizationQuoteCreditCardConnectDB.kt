package co.japl.android.finances.services.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.finances.services.interfaces.IConnectDB
import co.japl.android.finances.services.queries.ExtraValueAmortizationQuoteCreditCardQuery

class ExtraValueAmortizationQuoteCreditCardConnectDB: IConnectDB {
    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(this.javaClass.name,"<<<=== ExtraValueAmortizationQuoteCreditCardConnectDB#onCreate - Start")
        db?.execSQL(ExtraValueAmortizationQuoteCreditCardQuery.SQL_CREATE_ENTRIES)
        Log.i(this.javaClass.name,"<<<=== ExtraValueAmortizationQuoteCreditCardConnectDB#onCreate - END")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== ExtraValueAmortizationQuoteCreditCardConnectDB#upgrade - Start")
        if(oldVersion < ExtraValueAmortizationQuoteCreditCardQuery.DATA_BASE_VERSION_MINUS) {
            db?.execSQL(ExtraValueAmortizationQuoteCreditCardQuery.SQL_DELETE_ENTRIES)
            onCreate(db)
        }
        Log.i(this.javaClass.name,"<<<=== ExtraValueAmortizationQuoteCreditCardConnectDB#upgrade - END")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== ExtraValueAmortizationQuoteCreditCardConnectDB#downgrade - Start")
        db?.execSQL(ExtraValueAmortizationQuoteCreditCardQuery.SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.i(this.javaClass.name,"<<<=== ExtraValueAmortizationQuoteCreditCardConnectDB#downgrade - END")
    }
}