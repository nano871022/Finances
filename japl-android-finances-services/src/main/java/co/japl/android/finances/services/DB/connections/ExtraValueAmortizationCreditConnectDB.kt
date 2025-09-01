package co.japl.android.finances.services.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.finances.services.DB.connections.abstracs.DBRestore
import co.japl.android.finances.services.dto.ExtraValueAmortizationCreditDB
import co.japl.android.finances.services.interfaces.IConnectDB
import co.japl.android.finances.services.mapping.ExtraValueAmortizationCreditMap
import co.japl.android.finances.services.queries.ExtraValueAmortizationCreditQuery

class ExtraValueAmortizationCreditConnectDB: DBRestore(), IConnectDB {
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

    override fun onRestore(currentDB: SQLiteDatabase?, fromRestoreDB: SQLiteDatabase?) {
        onRestore(currentDB,fromRestoreDB,javaClass.simpleName,ExtraValueAmortizationCreditDB.Entry.TABLE_NAME,ExtraValueAmortizationCreditMap()::restore)
    }
    override fun onStats(connectionDB: SQLiteDatabase?): Pair<String, Long> {
        return onStats(connectionDB, ExtraValueAmortizationCreditDB.Entry.TABLE_NAME)
    }
}