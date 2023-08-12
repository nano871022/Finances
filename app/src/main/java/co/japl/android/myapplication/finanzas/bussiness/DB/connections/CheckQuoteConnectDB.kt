package co.japl.android.myapplication.bussiness.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.myapplication.bussiness.interfaces.IConnectDB
import co.japl.android.myapplication.finanzas.bussiness.queries.CheckQuoteQuery
import co.japl.android.myapplication.utils.DatabaseConstants
import java.lang.Exception

class CheckQuoteConnectDB:IConnectDB{

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

}