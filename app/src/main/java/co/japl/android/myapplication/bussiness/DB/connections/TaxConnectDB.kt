package co.japl.android.myapplication.bussiness.DB.connections

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import co.japl.android.myapplication.bussiness.queries.CreditCardBoughtQuery
import co.japl.android.myapplication.bussiness.queries.CreditCardQuery
import co.japl.android.myapplication.bussiness.queries.TaxQuery
import co.japl.android.myapplication.utils.DatabaseConstants

class TaxConnectDB(context: Context):SQLiteOpenHelper(context,
    DatabaseConstants.DATA_BASE_NAME,null, DatabaseConstants.DATA_BASE_VERSION) {

    override fun onCreate(p0: SQLiteDatabase?) {
        Log.i(this.javaClass.name,"<<<=== onCreate - Start")
        p0?.execSQL(TaxQuery.SQL_CREATE_ENTRIES)
        Log.i(this.javaClass.name,"<<<=== onCreate - End")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        Log.i(this.javaClass.name,"<<<=== onUpgrade - Start")
         p0?.execSQL(TaxQuery.SQL_DELETE_ENTRIES)
        onCreate(p0)
        Log.i(this.javaClass.name,"<<<=== onUpgrade - End")
    }

}