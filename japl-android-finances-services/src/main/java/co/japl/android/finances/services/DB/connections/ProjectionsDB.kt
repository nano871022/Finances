package co.japl.android.finances.services.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.finances.services.DB.connections.abstracs.DBRestore
import co.japl.android.finances.services.dto.ProjectionDB
import co.japl.android.finances.services.interfaces.IConnectDB
import co.japl.android.finances.services.mapping.ProjectionMap
import co.japl.android.finances.services.queries.ProjectionsQuery

class ProjectionsDB: DBRestore(), IConnectDB{

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

    override fun onRestore(currentDB: SQLiteDatabase?, fromRestoreDB: SQLiteDatabase?) {
        onRestore(currentDB,fromRestoreDB,javaClass.simpleName,ProjectionDB.Entry.TABLE_NAME,ProjectionMap()::restore)
    }

}