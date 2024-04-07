package co.japl.android.finances.services.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.finances.services.DB.connections.abstracs.DBRestore
import co.japl.android.finances.services.entities.SmsCreditCardDB
import co.japl.android.finances.services.interfaces.IConnectDB
import co.japl.android.finances.services.mapping.SmsCreditCardMap
import co.japl.android.finances.services.queries.SmsCreditCardQuery
import co.japl.android.finances.services.utils.DatabaseConstants
import java.lang.Exception

class SmsCreditCardConnectDB: DBRestore(), IConnectDB{

    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(this.javaClass.name,"<<<=== SmsCreditCardConnectDB#onCreate - Start")
        db?.execSQL(SmsCreditCardQuery.SQL_CREATE_ENTRIES)
        Log.i(this.javaClass.name,"<<<=== SmsCreditCardConnectDB#onCreate - End")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== SmsCreditCardConnectDB#onUpgrade - Start $oldVersion - $newVersion")
        if(oldVersion < SmsCreditCardQuery.DATA_BASE_VERSION_MINUS) {
            db?.execSQL(SmsCreditCardQuery.SQL_DELETE_ENTRIES)
            onCreate(db)
        }else{
            var findVersion = oldVersion+1
            while(findVersion <= newVersion) {
                try {
                    val value = findVersion.toString()
                    if(SmsCreditCardQuery.SQL_ALTER.containsKey(value)) {
                        SmsCreditCardQuery.SQL_ALTER[value]?.forEach { query ->
                            Log.d(this.javaClass.name, "Version $value query $query")
                            db?.execSQL(query)
                        }
                    }
                }catch (e:Exception){
                    Log.e(this.javaClass.name,"Exception $e continue $findVersion")
                }finally {
                    findVersion++
                }
            }
        }
        Log.i(this.javaClass.name,"<<<=== SmsCreditCardConnectDB#onUpgrade - End")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== SmsCreditCardConnectDB#onDowngrade - Start $oldVersion - $newVersion")
        db?.execSQL(SmsCreditCardQuery.SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.i(this.javaClass.name,"<<<=== SmsCreditCardConnectDB#onDowngrade - End")
    }

    override fun onRestore(currentDB: SQLiteDatabase?, fromRestoreDB: SQLiteDatabase?) {
        onRestore(currentDB,fromRestoreDB,javaClass.simpleName,SmsCreditCardDB.Entry.TABLE_NAME,SmsCreditCardMap()::restore)
    }

}