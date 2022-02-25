package co.japl.android.myapplication.bussiness.impl

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import co.japl.android.myapplication.bussiness.DTO.CalcDTO
import co.japl.android.myapplication.bussiness.SaveSvc
import co.japl.android.myapplication.utils.DatabaseConstants

class DBConnect(context: Context):SQLiteOpenHelper(context,
    DatabaseConstants.DATA_BASE_NAME,null, DatabaseConstants.DATA_BASE_VERSION) {

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(DatabaseConstants.SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
         p0?.execSQL(DatabaseConstants.SQL_DELETE_ENTRIES)
        onCreate(p0)
    }

}