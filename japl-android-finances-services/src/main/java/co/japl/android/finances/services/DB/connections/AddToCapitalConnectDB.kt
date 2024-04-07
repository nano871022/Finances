package co.japl.android.finances.services.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.finances.services.DB.connections.abstracs.DBRestore
import co.japl.android.finances.services.dto.AddToCapitalCreditDB
import co.japl.android.finances.services.interfaces.IConnectDB
import co.japl.android.finances.services.queries.AddToCapitalQuery

class AddToCapitalConnectDB: DBRestore(), IConnectDB {
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

    override fun onRestore(currentDB: SQLiteDatabase?, fromRestoreDB: SQLiteDatabase?) {

    }
}