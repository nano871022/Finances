package co.japl.android.finances.services.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.finances.services.DB.connections.abstracs.DBRestore
import co.japl.android.finances.services.dto.CheckQuoteDB
import co.japl.android.finances.services.interfaces.IConnectDB
import co.japl.android.finances.services.mapping.CheckQuoteMap
import co.japl.android.finances.services.queries.CheckQuoteQuery
import java.lang.Exception

class CheckQuoteConnectDB: DBRestore(),IConnectDB{

    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(this.javaClass.name,"<<<=== CheckQuoteConnectDB#onCreate - Start")
        db?.execSQL(CheckQuoteQuery.SQL_CREATE_ENTRIES)
        Log.i(this.javaClass.name,"<<<=== CheckQuoteConnectDB#onCreate - End")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== CheckQuoteConnectDB#onUpgrade - Start $oldVersion - $newVersion")
        if(oldVersion < CheckQuoteQuery.DATA_BASE_VERSION_MINUS) {
            db?.execSQL(CheckQuoteQuery.SQL_DELETE_ENTRIES)
            onCreate(db)
        }else{
            var findVersion = oldVersion+1
            while(findVersion <= newVersion) {
                try {
                    val value = findVersion.toString()
                    /*
                    if(CheckQuoteQuery.SQL_ALTER.containsKey(value)) {
                        val query = CheckQuoteQuery.SQL_ALTER[value]
                        Log.d(this.javaClass.name, "Version $value query $query")
                        db?.execSQL(query)
                    }
                    */
                }catch (e:Exception){
                    Log.e(this.javaClass.name,"Exception $e continue $findVersion")
                }finally {
                    findVersion++
                }
            }
        }
        Log.i(this.javaClass.name,"<<<=== CheckQuoteConnectDB#onUpgrade - End")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== CheckQuoteConnectDB#onDowngrade - Start $oldVersion - $newVersion")
        db?.execSQL(CheckQuoteQuery.SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.i(this.javaClass.name,"<<<=== CheckQuoteConnectDB#onDowngrade - End")
    }

    override fun onRestore(currentDB: SQLiteDatabase?, fromRestoreDB: SQLiteDatabase?) {
        onRestore(currentDB,fromRestoreDB,javaClass.simpleName,CheckQuoteDB.Entry.TABLE_NAME,CheckQuoteMap()::restore)
    }
    override fun onStats(connectionDB: SQLiteDatabase?): Pair<String, Long> {
        return onStats(connectionDB, CheckQuoteDB.Entry.TABLE_NAME)
    }
}