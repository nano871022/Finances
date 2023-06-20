package co.japl.android.myapplication.finanzas.bussiness.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.myapplication.bussiness.interfaces.IConnectDB
import co.japl.android.myapplication.finanzas.bussiness.queries.CreditQuery
import co.japl.android.myapplication.finanzas.bussiness.queries.PaidQuery
import co.japl.android.myapplication.utils.DatabaseConstants

class PaidConnectDB: IConnectDB {
    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(this.javaClass.name,"<<<=== PaidConnectDB#onCreate - Start")
        db?.execSQL(PaidQuery.SQL_CREATE_ENTRIES)
        Log.i(this.javaClass.name,"<<<=== PaidConnectDB#onCreate - END")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== PaidConnectDB#upgrade - Start")
        if(oldVersion < PaidQuery.DATA_BASE_VERSION_MINUS) {
            db?.execSQL(PaidQuery.SQL_DELETE_ENTRIES)
            onCreate(db)
        }else{
            update(oldVersion,newVersion,PaidQuery.SQL_ALTER_TABLE){
                db?.execSQL(it)
            }
        }
        Log.i(this.javaClass.name,"<<<=== PaidConnectDB#upgrade - END")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== PaidConnectDB#downgrade - Start")
        db?.execSQL(PaidQuery.SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.i(this.javaClass.name,"<<<=== PaidConnectDB#downgrade - END")
    }
}