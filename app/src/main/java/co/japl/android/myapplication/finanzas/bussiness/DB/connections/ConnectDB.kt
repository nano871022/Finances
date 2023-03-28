package co.japl.android.myapplication.bussiness.DB.connections

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import co.japl.android.myapplication.finanzas.bussiness.DB.connections.AddToCapitalConnectDB
import co.japl.android.myapplication.finanzas.bussiness.DB.connections.AdditionalCreditConnectDB
import co.japl.android.myapplication.finanzas.bussiness.DB.connections.CreditFixConnectDB
import co.japl.android.myapplication.finanzas.bussiness.queries.AddToCapitalQuery
import co.japl.android.myapplication.utils.DatabaseConstants

class ConnectDB(context: Context):SQLiteOpenHelper(context,
    DatabaseConstants.DATA_BASE_NAME,null, 27) {

    override fun onCreate(p0: SQLiteDatabase?) {
        Log.i(this.javaClass.name,"<<<=== onCreate - Start $p0")
        CalculationConnectDB().onCreate(p0)
        CreditCardConnectDB().onCreate(p0)
        CreditCardBoughtConnectDB().onCreate(p0)
        TaxConnectDB().onCreate(p0)
        CreditCardSettingConnectDB().onCreate(p0);
        BuyCreditCardSettingConnectDB().onCreate(p0);
        CreditFixConnectDB().onCreate(p0)
        AdditionalCreditConnectDB().onCreate(p0)
        AddToCapitalConnectDB().onCreate(p0)
        Log.i(this.javaClass.name,"<<<=== onCreate - End")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        Log.i(this.javaClass.name,"<<<=== onUpgrade - Start $p1 - $p2")
        CalculationConnectDB().onUpgrade(p0,p1,p2)
        CreditCardConnectDB().onUpgrade(p0,p1,p2)
        CreditCardBoughtConnectDB().onUpgrade(p0,p1,p2)
        TaxConnectDB().onUpgrade(p0,p1,p2)
        CreditCardSettingConnectDB().onUpgrade(p0,p1,p2)
        BuyCreditCardSettingConnectDB().onUpgrade(p0,p1,p2)
        CreditFixConnectDB().onUpgrade(p0,p1,p2)
        AdditionalCreditConnectDB().onUpgrade(p0,p1,p2)
        AddToCapitalConnectDB().onUpgrade(p0,p1,p2)
        Log.i(this.javaClass.name,"<<<=== onUpgrade - End")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== onDowngrade - Start $oldVersion - $newVersion")
        CalculationConnectDB().onDowngrade(db,oldVersion,newVersion)
        CreditCardConnectDB().onDowngrade(db,oldVersion,newVersion)
        CreditCardBoughtConnectDB().onDowngrade(db,oldVersion,newVersion)
        TaxConnectDB().onDowngrade(db,oldVersion,newVersion)
        CreditCardSettingConnectDB().onDowngrade(db,oldVersion,newVersion)
        BuyCreditCardSettingConnectDB().onDowngrade(db,oldVersion,newVersion)
        CreditFixConnectDB().onDowngrade(db,oldVersion,newVersion)
        AdditionalCreditConnectDB().onDowngrade(db,oldVersion,newVersion)
        AddToCapitalConnectDB().onDowngrade(db,oldVersion,newVersion)
        Log.i(this.javaClass.name,"<<<=== onDowngrade - End")
    }

}