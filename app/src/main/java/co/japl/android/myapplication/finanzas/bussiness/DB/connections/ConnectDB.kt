package co.japl.android.myapplication.finanzas.bussiness.DB.connections

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import co.japl.android.finances.services.DB.connections.ConnectDB as ServicesConnectDB
import co.japl.android.myapplication.utils.DatabaseConstants

class ConnectDB(private val context: Context):SQLiteOpenHelper(context,
        DatabaseConstants.DATA_BASE_NAME,null, 4_08_01_175) {

    val connectDB:ServicesConnectDB = ServicesConnectDB(context)
    override fun onCreate(p0: SQLiteDatabase?) {
        connectDB.onCreate(p0)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        connectDB.onUpgrade(p0,p1,p2)
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
      connectDB.onDowngrade(db,oldVersion,newVersion)
    }

    fun onRestore(currentDb: SQLiteDatabase?, fromRestoreDb: SQLiteDatabase?) {
        connectDB.onRestore(currentDb,fromRestoreDb)
    }

}