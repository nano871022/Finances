package co.japl.android.finances.services.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.finances.services.DB.connections.abstracs.DBRestore
import co.japl.android.finances.services.dto.CreditCardBoughtDB
import co.japl.android.finances.services.interfaces.IConnectDB
import co.japl.android.finances.services.mapping.CreditCardBoughtMap
import co.japl.android.finances.services.queries.CreditCardBoughtQuery
import co.japl.android.finances.services.utils.DatabaseConstants

class CreditCardBoughtConnectDB: DBRestore(), IConnectDB {

    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(this.javaClass.name,"<<<=== CreditCardBoughtConnectDB#onCreate - Start")
        db?.execSQL(CreditCardBoughtQuery.SQL_CREDIT_CARD_CREATE_ENTRIES)
        Log.i(this.javaClass.name,"<<<=== CreditCardBoughtConnectDB#onCreate - End")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== CreditCardBoughtConnectDB#onUpgrade - Start")
        if(oldVersion <  DatabaseConstants.DATA_BASE_VERSION_MINUS) {
            db?.execSQL(CreditCardBoughtQuery.SQL_CREDIT_CARD_DELETE_ENTRIES)
            onCreate(db)
        }else{
            var findVersion = oldVersion + 1
            while (findVersion <= newVersion){
                try {
                    val value = findVersion.toString()
                    if (CreditCardBoughtQuery.SQL_ALTER.containsKey(value)) {
                        val query = CreditCardBoughtQuery.SQL_ALTER[value]
                        db?.execSQL(query)
                    }
                }catch(e:Exception){
                    Log.e(javaClass.name,"Exception $e continue $findVersion")
                }finally{
                    findVersion++
                }
            }
        }
        Log.i(this.javaClass.name,"<<<=== CreditCardBoughtConnectDB#onUpgrade - End")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== CreditCardBoughtConnectDB#onDowngrade - Start $oldVersion - $newVersion")
        db?.execSQL(CreditCardBoughtQuery.SQL_CREDIT_CARD_DELETE_ENTRIES)
        onCreate(db)
        Log.i(this.javaClass.name,"<<<=== CreditCardBoughtConnectDB#onDowngrade - End")
    }

    override fun onRestore(currentDB: SQLiteDatabase?, fromRestoreDB: SQLiteDatabase?) {
        onRestore(currentDB,fromRestoreDB,javaClass.simpleName,CreditCardBoughtDB.CreditCardBoughtEntry.TABLE_NAME,CreditCardBoughtMap()::restore)
    }


}