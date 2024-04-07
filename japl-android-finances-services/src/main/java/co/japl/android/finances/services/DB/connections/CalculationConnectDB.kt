package co.japl.android.finances.services.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.finances.services.DB.connections.abstracs.DBRestore
import co.japl.android.finances.services.interfaces.IConnectDB
import co.japl.android.finances.services.queries.CalculationQuery
import co.japl.android.finances.services.utils.DatabaseConstants

class CalculationConnectDB : DBRestore(), IConnectDB{

    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(this.javaClass.name,"<<<=== CalculationConnectDB#OnCreate - Start")
        db?.execSQL(CalculationQuery.SQL_CREATE_ENTRIES)
        Log.i(this.javaClass.name,"<<<=== CalculationConnectDB#OnCreate - End")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== CalculationConnectDB#OnUpgrade - Start")
        if(oldVersion <  DatabaseConstants.DATA_BASE_VERSION_MINUS) {
            db?.execSQL(CalculationQuery.SQL_DELETE_ENTRIES)
            onCreate(db)
        }else{
            var findVersion = oldVersion + 1
            while (findVersion <= newVersion){
                try {
                    val value = findVersion.toString()
                    if (CalculationQuery.SQL_ALTER.containsKey(value)) {
                        val query = CalculationQuery.SQL_ALTER[value]
                        db?.execSQL(query)
                    }
                }catch(e:Exception){
                    Log.e(javaClass.name,"Exception $e continue $findVersion")
                }finally{
                    findVersion++
                }
            }
        }
        Log.i(this.javaClass.name,"<<<=== CalculationConnectDB#OnUpgrade - End")
    }
    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== CalculationConnectDB#onDowngrade - Start $oldVersion - $newVersion")
        db?.execSQL(CalculationQuery.SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.i(this.javaClass.name,"<<<=== CalculationConnectDB#onDowngrade - End")
    }

    override fun onRestore(currentDB: SQLiteDatabase?, fromRestoreDB: SQLiteDatabase?) {
    }


}