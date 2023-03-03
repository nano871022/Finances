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
        Log.i(this.javaClass.name,"<<<===  BuyCreditCardSettingConnectDB#onCreate - Start")
        db?.execSQL(BuyCreditCardSettingQuery.SQL_CREATE_ENTRIES)
        Log.i(this.javaClass.name,"<<<===  BuyCreditCardSettingConnectDB#onCreate - End")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== BuyCreditCardSettingConnectDB# onUpgrade - Start $oldVersion - $newVersion")
        if(oldVersion < DatabaseConstants.DATA_BASE_VERSION_MINUS_SETTING) {
            db?.execSQL(BuyCreditCardSettingQuery.SQL_DELETE_ENTRIES)
            onCreate(db)
        }
        Log.i(this.javaClass.name,"<<<===  BuyCreditCardSettingConnectDB#onUpgrade - End")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<===  BuyCreditCardSettingConnectDB#onDowngrade - Start $oldVersion - $newVersion")
        db?.execSQL(BuyCreditCardSettingQuery.SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.i(this.javaClass.name,"<<<===  BuyCreditCardSettingConnectDB#onDowngrade - End")
    }

}