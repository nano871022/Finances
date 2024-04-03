package co.japl.android.finances.services.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.finances.services.interfaces.IConnectDB
import co.japl.android.finances.services.queries.SmsPaidQuery
import java.lang.Exception

class SmsPaidConnectDB:IConnectDB{

    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(this.javaClass.name,"<<<=== SmsPaidConnectDB#onCreate - Start")
        db?.execSQL(SmsPaidQuery.SQL_CREATE_ENTRIES)
        Log.i(this.javaClass.name,"<<<=== SmsPaidConnectDB#onCreate - End")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== SmsPaidConnectDB#onUpgrade - Start $oldVersion - $newVersion")
        if(oldVersion < SmsPaidQuery.DATA_BASE_VERSION_MINUS) {
            db?.execSQL(SmsPaidQuery.SQL_DELETE_ENTRIES)
            onCreate(db)
        }else{
            var findVersion = oldVersion+1
            while(findVersion <= newVersion) {
                try {
                    val value = findVersion.toString()
                    if(SmsPaidQuery.SQL_ALTER.containsKey(value)) {
                        SmsPaidQuery.SQL_ALTER[value]?.forEach { query ->
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
        Log.i(this.javaClass.name,"<<<=== SmsPaidConnectDB#onUpgrade - End")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== SmsPaidConnectDB#onDowngrade - Start $oldVersion - $newVersion")
        db?.execSQL(SmsPaidQuery.SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.i(this.javaClass.name,"<<<=== SmsPaidConnectDB#onDowngrade - End")
    }

}