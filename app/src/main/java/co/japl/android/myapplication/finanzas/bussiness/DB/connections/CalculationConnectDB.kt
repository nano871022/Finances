package co.japl.android.myapplication.bussiness.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.myapplication.bussiness.interfaces.IConnectDB
import co.japl.android.myapplication.bussiness.queries.CalculationQuery
import co.japl.android.myapplication.utils.DatabaseConstants

class CalculationConnectDB : IConnectDB{

    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(this.javaClass.name,"<<<=== CalculationConnectDB#OnCreate - Start")
        db?.execSQL(CalculationQuery.SQL_CREATE_ENTRIES)
        Log.i(this.javaClass.name,"<<<=== CalculationConnectDB#OnCreate - End")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== CalculationConnectDB#OnUpgrade - Start")
        if(oldVersion <  DatabaseConstants.DATA_BASE_VERSION_MINUS) {
            db?.execSQL(CalculationQuery.SQL_DELETE_ENTRIES)
            onCreate(db)
        }
        Log.i(this.javaClass.name,"<<<=== CalculationConnectDB#OnUpgrade - End")
    }
    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== CalculationConnectDB#onDowngrade - Start $oldVersion - $newVersion")
        db?.execSQL(CalculationQuery.SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.i(this.javaClass.name,"<<<=== CalculationConnectDB#onDowngrade - End")
    }


}