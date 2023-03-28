package co.japl.android.myapplication.bussiness.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.myapplication.bussiness.interfaces.IConnectDB
import co.japl.android.myapplication.bussiness.queries.CalculationQuery
import co.japl.android.myapplication.bussiness.queries.CreditCardBoughtQuery
import co.japl.android.myapplication.utils.DatabaseConstants

class CreditCardBoughtConnectDB:IConnectDB {

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


}