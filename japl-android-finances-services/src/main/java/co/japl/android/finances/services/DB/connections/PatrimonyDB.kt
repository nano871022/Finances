package co.japl.android.finances.services.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.finances.services.DB.connections.abstracs.DBRestore
import co.japl.android.finances.services.core.mapper.PaidMapper
import co.japl.android.finances.services.dao.implement.PaidImpl
import co.japl.android.finances.services.dto.PaidDB
import co.japl.android.finances.services.dto.PatrimonyDB
import co.japl.android.finances.services.interfaces.IConnectDB
import co.japl.android.finances.services.mapping.PaidMap
import co.japl.android.finances.services.mapping.PatrimonyMap
import co.japl.android.finances.services.queries.PaidQuery
import co.japl.android.finances.services.queries.PatrimonyQuery

class PatrimonyDB: DBRestore(), IConnectDB {
    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(this.javaClass.name,"<<<=== PatrimonyDB#onCreate - Start")
        db?.execSQL(PatrimonyQuery.SQL_CREATE_ENTRIES)
        Log.i(this.javaClass.name,"<<<=== PatrimonyDB#onCreate - END")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== PatrimonyDB#upgrade - Start")
        if(oldVersion < PatrimonyQuery.DATA_BASE_VERSION_MINUS) {
            db?.execSQL(PatrimonyQuery.SQL_DELETE_ENTRIES)
            onCreate(db)
        }else{
            /*
            update(oldVersion,newVersion,PatrimonyQuery.SQL_ALTER_TABLE){
                db?.execSQL(it)
            }
            */
        }
        Log.i(this.javaClass.name,"<<<=== PatrimonyDB#upgrade - END")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== PatrimonyDB#downgrade - Start")
        db?.execSQL(PatrimonyQuery.SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.i(this.javaClass.name,"<<<=== PatrimonyDB#downgrade - END")
    }

    override fun onRestore(currentDB:SQLiteDatabase?,fromRestoreDB:SQLiteDatabase?){
        onRestore(currentDB,fromRestoreDB,"PatrimonyDB",PatrimonyDB.PatrimonyEntry.TABLE_NAME,
            PatrimonyMap::restore)
    }

    override fun onStats(connectionDB: SQLiteDatabase?): Pair<String, Long> {
        return onStats(connectionDB, PatrimonyDB.PatrimonyEntry.TABLE_NAME)
    }
}