package co.japl.android.finances.services.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.finances.services.DB.connections.abstracs.DBRestore
import co.japl.android.finances.services.dto.BuyCreditCardSettingDB
import co.japl.android.finances.services.interfaces.IConnectDB
import co.japl.android.finances.services.mapping.BuyCreditCardSettingMap
import co.japl.android.finances.services.queries.BuyCreditCardSettingQuery
import co.japl.android.finances.services.utils.DatabaseConstants

class BuyCreditCardSettingConnectDB: DBRestore(), IConnectDB{

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

    override fun onRestore(currentDB: SQLiteDatabase?, fromRestoreDB: SQLiteDatabase?) {
         onRestore(currentDB,fromRestoreDB,javaClass.simpleName,BuyCreditCardSettingDB.Entry.TABLE_NAME,BuyCreditCardSettingMap()::restore)
    }

    override fun onStats(connectionDB: SQLiteDatabase?): Pair<String, Long> {
        return onStats(connectionDB, BuyCreditCardSettingDB.Entry.TABLE_NAME)
    }

}