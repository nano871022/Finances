package co.japl.android.finances.services.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.finances.services.DB.connections.abstracs.DBRestore
import co.japl.android.finances.services.dto.CreditCardDB
import co.japl.android.finances.services.interfaces.IConnectDB
import co.japl.android.finances.services.mapping.CreditCardMap
import co.japl.android.finances.services.queries.CreditCardQuery
import co.japl.android.finances.services.utils.DatabaseConstants

class CreditCardConnectDB: DBRestore(), IConnectDB {

    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(this.javaClass.name,"<<<=== CreditCardConnectDB#onCreate - Start")
        val query = CreditCardQuery.SQL_CREATE_ENTRIES
        db?.execSQL(query)
        Log.i(this.javaClass.name,"<<<=== CreditCardConnectDB#onCreate - End")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== CreditCardConnectDB#onUpgrade - Start")
        if(oldVersion <  DatabaseConstants.DATA_BASE_VERSION_MINUS) {
            db?.execSQL(CreditCardQuery.SQL_DELETE_ENTRIES)
            onCreate(db)
        }else {
            TaxConnectDB().update(oldVersion, newVersion, CreditCardQuery.SQL_ALTER) {
                db?.execSQL(it)
            }
        }
        Log.i(this.javaClass.name,"<<<=== CreditCardConnectDB#onUpgrade - End")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== CreditCardConnectDB#onDowngrade - Start $oldVersion - $newVersion")
        db?.execSQL(CreditCardQuery.SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.i(this.javaClass.name,"<<<=== CreditCardConnectDB#onDowngrade - End")
    }

    override fun onRestore(currentDB: SQLiteDatabase?, fromRestoreDB: SQLiteDatabase?) {
        onRestore(currentDB,fromRestoreDB,javaClass.simpleName,CreditCardDB.CreditCardEntry.TABLE_NAME,CreditCardMap()::restore)
    }


}