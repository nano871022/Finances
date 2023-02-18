package co.japl.android.myapplication.bussiness.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.myapplication.bussiness.interfaces.IConnectDB
import co.japl.android.myapplication.bussiness.queries.BuyCreditCardSettingQuery
import co.japl.android.myapplication.bussiness.queries.CreditCardSettingQuery
import co.japl.android.myapplication.bussiness.queries.TaxQuery
import co.japl.android.myapplication.utils.DatabaseConstants
import java.lang.Exception

class BuyCreditCardSettingConnectDB:IConnectDB{

    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(this.javaClass.name,"<<<=== onCreate - Start")
        db?.execSQL(BuyCreditCardSettingQuery.SQL_CREATE_ENTRIES)
        Log.i(this.javaClass.name,"<<<=== onCreate - End")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== onUpgrade - Start $oldVersion - $newVersion")
        if(oldVersion < DatabaseConstants.DATA_BASE_VERSION_MINUS) {
            db?.execSQL(BuyCreditCardSettingQuery.SQL_DELETE_ENTRIES)
            onCreate(db)
        }/*else{
            var findVersion = oldVersion+1
            while(findVersion <= newVersion) {
                try {
                    val value = findVersion.toString()
                    val query = CreditCardSettingQuery.SQL_ALTER[value]
                    Log.d(this.javaClass.name, "Version $value query $query")
                    db?.execSQL(query)
                }catch (e:Exception){
                    Log.e(this.javaClass.name,"Exception $e continue $findVersion")
                }finally {
                    findVersion++
                }

        }*/
        Log.i(this.javaClass.name,"<<<=== onUpgrade - End")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== onDowngrade - Start $oldVersion - $newVersion")
        db?.execSQL(BuyCreditCardSettingQuery.SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.i(this.javaClass.name,"<<<=== onDowngrade - End")
    }

}