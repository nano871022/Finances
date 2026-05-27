package co.japl.android.finances.services.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.finances.services.DB.connections.abstracs.DBRestore
import co.japl.android.finances.services.core.mapper.PaidMapper
import co.japl.android.finances.services.dao.implement.PaidImpl
import co.japl.android.finances.services.dto.PaidDB
import co.japl.android.finances.services.dto.TaxHistoryDB
import co.japl.android.finances.services.interfaces.IConnectDB
import co.japl.android.finances.services.mapping.PaidMap
import co.japl.android.finances.services.mapping.TaxHistoryMap
import co.japl.android.finances.services.queries.PaidQuery
import co.japl.android.finances.services.queries.TaxHistoryQuery

class TaxHistoryDB: DBRestore(), IConnectDB {
    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(this.javaClass.name,"<<<=== TaxHistoryDB#onCreate - Start")
        db?.execSQL(TaxHistoryQuery.SQL_CREATE_ENTRIES)
        Log.i(this.javaClass.name,"<<<=== TaxHistoryDB#onCreate - END")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== TaxHistoryDB#upgrade - Start")
        if(oldVersion < TaxHistoryQuery.DATA_BASE_VERSION_MINUS) {
            db?.execSQL(TaxHistoryQuery.SQL_DELETE_ENTRIES)
            onCreate(db)
        }else{
            /*
            update(oldVersion,newVersion,TaxHistoryQuery.SQL_ALTER_TABLE){
                db?.execSQL(it)
            }
            */
        }
        Log.i(this.javaClass.name,"<<<=== TaxHistoryDB#upgrade - END")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== TaxHistoryDB#downgrade - Start")
        db?.execSQL(TaxHistoryQuery.SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.i(this.javaClass.name,"<<<=== TaxHistoryDB#downgrade - END")
    }

    override fun onRestore(currentDB:SQLiteDatabase?,fromRestoreDB:SQLiteDatabase?){
        onRestore(currentDB,fromRestoreDB,"TaxHistoryDB",TaxHistoryDB.TaxHistoryEntry.TABLE_NAME,
            TaxHistoryMap::restore)
    }

    override fun onStats(connectionDB: SQLiteDatabase?): Pair<String, Long> {
        return onStats(connectionDB, TaxHistoryDB.TaxHistoryEntry.TABLE_NAME)
    }
}