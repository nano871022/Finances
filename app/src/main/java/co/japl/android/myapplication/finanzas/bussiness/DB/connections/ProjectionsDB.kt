package co.japl.android.myapplication.bussiness.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.myapplication.bussiness.interfaces.IConnectDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.ProjectionDB
import co.japl.android.myapplication.finanzas.bussiness.queries.ProjectionsQuery
import co.japl.android.myapplication.utils.DatabaseConstants
import java.lang.Exception

class ProjectionsDB:IConnectDB{

    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(this.javaClass.name,"<<<=== ProjectionsDB#onCreate - Start")
        db?.execSQL(ProjectionsQuery.SQL_CREATE_ENTRIES)
        Log.i(this.javaClass.name,"<<<=== ProjectionsDB#onCreate - End")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== ProjectionsDB#onUpgrade - Start $oldVersion - $newVersion")
        if(oldVersion < ProjectionsQuery.DATA_BASE_VERSION_MINUS) {
            db?.execSQL(ProjectionsQuery.SQL_DELETE_ENTRIES)
            onCreate(db)
        }
        Log.i(this.javaClass.name,"<<<=== ProjectionsDB#onUpgrade - End")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== ProjectionsDB#onDowngrade - Start $oldVersion - $newVersion")
        db?.execSQL(ProjectionsQuery.SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.i(this.javaClass.name,"<<<=== ProjectionsDB#onDowngrade - End")
    }

}