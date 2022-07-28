package co.japl.android.myapplication.bussiness.DB.connections

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import co.japl.android.myapplication.bussiness.DTO.CreditCardBoughtDB
import co.japl.android.myapplication.bussiness.queries.CreditCardBoughtQuery
import co.japl.android.myapplication.bussiness.queries.CreditCardQuery
import co.japl.android.myapplication.bussiness.queries.TaxQuery
import co.japl.android.myapplication.utils.DatabaseConstants

class ConnectDB(context: Context):SQLiteOpenHelper(context,
    DatabaseConstants.DATA_BASE_NAME,null, DatabaseConstants.DATA_BASE_VERSION) {

    override fun onCreate(p0: SQLiteDatabase?) {
        Log.i(this.javaClass.name,"<<<=== onCreate - Start")
        CalculationConnectDB().onCreate(p0)
        CreditCardConnectDB().onCreate(p0)
        CreditCardBoughtConnectDB().onCreate(p0)
        TaxConnectDB().onCreate(p0)
        Log.i(this.javaClass.name,"<<<=== onCreate - End")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        Log.i(this.javaClass.name,"<<<=== onUpgrade - Start $p1 - $p2")
        CalculationConnectDB().onUpgrade(p0,p1,p2)
        CreditCardConnectDB().onUpgrade(p0,p1,p2)
        CreditCardBoughtConnectDB().onUpgrade(p0,p1,p2)
        TaxConnectDB().onCreate(p0)
        Log.i(this.javaClass.name,"<<<=== onUpgrade - End")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== onDowngrade - Start $oldVersion - $newVersion")
        CalculationConnectDB().onDowngrade(db,oldVersion,newVersion)
        CreditCardConnectDB().onDowngrade(db,oldVersion,newVersion)
        CreditCardBoughtConnectDB().onDowngrade(db,oldVersion,newVersion)
        TaxConnectDB().onDowngrade(db,oldVersion,newVersion)
        Log.i(this.javaClass.name,"<<<=== onDowngrade - End")
    }

}