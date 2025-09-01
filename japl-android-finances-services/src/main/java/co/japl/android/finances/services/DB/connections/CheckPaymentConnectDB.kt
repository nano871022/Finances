package co.japl.android.finances.services.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.finances.services.DB.connections.abstracs.DBRestore
import co.japl.android.finances.services.dto.CheckPaymentsDB
import co.japl.android.finances.services.interfaces.IConnectDB
import co.japl.android.finances.services.mapping.CheckPaymentsMap
import co.japl.android.finances.services.queries.CheckPaymentsQuery
import java.lang.Exception

class CheckPaymentConnectDB: DBRestore(),IConnectDB{

    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(this.javaClass.name,"<<<=== CheckPaymentsConnectDB#onCreate - Start")
        db?.execSQL(CheckPaymentsQuery.SQL_CREATE_ENTRIES)
        Log.i(this.javaClass.name,"<<<=== CheckPaymentsConnectDB#onCreate - End")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== CheckPaymentsConnectDB#onUpgrade - Start $oldVersion - $newVersion")
        if(oldVersion < CheckPaymentsQuery.DATA_BASE_VERSION_MINUS) {
            db?.execSQL(CheckPaymentsQuery.SQL_DELETE_ENTRIES)
            onCreate(db)
        }else{
            var findVersion = oldVersion+1
            while(findVersion <= newVersion) {
                try {
                    val value = findVersion.toString()
                    /*
                    if(CheckPaymentsQuery.SQL_ALTER.containsKey(value)) {
                        val query = CheckPaymentsQuery.SQL_ALTER[value]
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
        Log.i(this.javaClass.name,"<<<=== CheckPaymentsConnectDB#onUpgrade - End")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== CheckPaymentsConnectDB#onDowngrade - Start $oldVersion - $newVersion")
        db?.execSQL(CheckPaymentsQuery.SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.i(this.javaClass.name,"<<<=== CheckPaymentsConnectDB#onDowngrade - End")
    }

    override fun onRestore(currentDB: SQLiteDatabase?, fromRestoreDB: SQLiteDatabase?) {
        onRestore(currentDB,fromRestoreDB,javaClass.simpleName,CheckPaymentsDB.Entry.TABLE_NAME,CheckPaymentsMap()::restore)
    }
    override fun onStats(connectionDB: SQLiteDatabase?): Pair<String, Long> {
        return onStats(connectionDB, CheckPaymentsDB.Entry.TABLE_NAME)
    }

}