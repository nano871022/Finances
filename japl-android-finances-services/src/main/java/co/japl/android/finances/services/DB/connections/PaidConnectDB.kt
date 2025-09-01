package co.japl.android.finances.services.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.finances.services.DB.connections.abstracs.DBRestore
import co.japl.android.finances.services.core.mapper.PaidMapper
import co.japl.android.finances.services.dao.implement.PaidImpl
import co.japl.android.finances.services.dto.PaidDB
import co.japl.android.finances.services.interfaces.IConnectDB
import co.japl.android.finances.services.mapping.PaidMap
import co.japl.android.finances.services.queries.PaidQuery

class PaidConnectDB: DBRestore(), IConnectDB {
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

    override fun onRestore(currentDB:SQLiteDatabase?,fromRestoreDB:SQLiteDatabase?){
        onRestore(currentDB,fromRestoreDB,"PaidConnectDB",PaidDB.Entry.TABLE_NAME,PaidMap()::restore)
    }

    override fun onStats(connectionDB: SQLiteDatabase?): Pair<String, Long> {
        return onStats(connectionDB, PaidDB.Entry.TABLE_NAME)
    }
}