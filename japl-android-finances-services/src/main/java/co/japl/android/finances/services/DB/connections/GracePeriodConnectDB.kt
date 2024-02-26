package co.japl.android.finances.services.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.finances.services.interfaces.IConnectDB
import co.japl.android.finances.services.queries.GracePeriodQuery

class GracePeriodConnectDB: IConnectDB {
    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(this.javaClass.name,"<<<=== GracePeriodDB#onCreate - Start")
        db?.execSQL(GracePeriodQuery.SQL_CREATE_ENTRIES)
        Log.i(this.javaClass.name,"<<<=== GracePeriodDB#onCreate - END")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== GracePeriodDB#upgrade - Start")
        if(oldVersion < GracePeriodQuery.DATA_BASE_VERSION_MINUS) {
            db?.execSQL(GracePeriodQuery.SQL_DELETE_ENTRIES)
            onCreate(db)
        }
        Log.i(this.javaClass.name,"<<<=== GracePeriodDB#upgrade - END")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== GracePeriodDB#downgrade - Start")
        db?.execSQL(GracePeriodQuery.SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.i(this.javaClass.name,"<<<=== GracePeriodDB#downgrade - END")
    }
}