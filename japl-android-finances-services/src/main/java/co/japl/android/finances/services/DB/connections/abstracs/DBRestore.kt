package co.japl.android.finances.services.DB.connections.abstracs

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.finances.services.dto.PaidDB
import co.japl.android.finances.services.mapping.PaidMap

abstract class DBRestore {

    fun onRestore(currentDB: SQLiteDatabase?, fromRestoreDB: SQLiteDatabase?,nameConnect:String,nameTable:String,contentValues:(Cursor)->ContentValues){
        Log.i(this.javaClass.name,"<<<=== $nameConnect#onRestore - Start")
        currentDB?.execSQL("DELETE FROM $nameTable ")
        fromRestoreDB?.query(
            nameTable,
            null,
            null,
            null,
            null,
            null,
            null
        )?.use {
            while(it.moveToNext()){
                val contentValues = contentValues.invoke(it)
                currentDB?.insert(nameTable,null,contentValues)
            }
        }
        Log.i(this.javaClass.name,"<<<=== $nameConnect#onRestore - END")
    }
}