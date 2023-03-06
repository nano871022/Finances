package co.japl.android.myapplication.bussiness.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.myapplication.bussiness.interfaces.IConnectDB
import co.japl.android.myapplication.bussiness.queries.CreditCardSettingQuery
import co.japl.android.myapplication.bussiness.queries.TaxQuery
import co.japl.android.myapplication.utils.DatabaseConstants
import java.lang.Exception

class CreditCardSettingConnectDB:IConnectDB{

    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(this.javaClass.name,"<<<=== CreditCardSetting#onCreate - Start")
        db?.execSQL(CreditCardSettingQuery.SQL_CREATE_ENTRIES)
        Log.i(this.javaClass.name,"<<<=== CreditCardSetting#onCreate - End")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== CreditCardSetting#onUpgrade - Start $oldVersion - $newVersion")
        if(oldVersion < DatabaseConstants.DATA_BASE_VERSION_MINUS_SETTING) {
            db?.execSQL(CreditCardSettingQuery.SQL_DELETE_ENTRIES)
            onCreate(db)
        }
        Log.i(this.javaClass.name,"<<<=== CreditCardSetting#onUpgrade - End")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== CreditCardSetting#onDowngrade - Start $oldVersion - $newVersion")
        db?.execSQL(CreditCardSettingQuery.SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.i(this.javaClass.name,"<<<=== CreditCardSetting#onDowngrade - End")
    }

}