package co.japl.android.finanzas.bussiness.DB.connections

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import co.japl.android.finanzas.bussiness.interfaces.IConnectDB
import co.japl.android.finanzas.bussiness.queries.CreditCardBoughtQuery
import co.japl.android.finanzas.bussiness.queries.CreditCardQuery
import co.japl.android.finanzas.utils.DatabaseConstants

class CreditCardConnectDB: IConnectDB {

    override fun onCreate(p0: SQLiteDatabase?) {
        Log.i(this.javaClass.name,"<<<=== onCreate - Start")
        val query = CreditCardQuery.SQL_CREATE_ENTRIES
        p0?.execSQL(query)
        Log.i(this.javaClass.name,"<<<=== onCreate - End")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        Log.i(this.javaClass.name,"<<<=== onUpgrade - Start")
         p0?.execSQL(CreditCardQuery.SQL_DELETE_ENTRIES)
        onCreate(p0)
        Log.i(this.javaClass.name,"<<<=== onUpgrade - End")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== onDowngrade - Start $oldVersion - $newVersion")
        db?.execSQL(CreditCardQuery.SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.i(this.javaClass.name,"<<<=== onDowngrade - End")
    }


    }