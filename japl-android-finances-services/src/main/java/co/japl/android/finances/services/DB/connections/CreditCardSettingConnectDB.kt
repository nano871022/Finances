package co.japl.android.finances.services.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.finances.services.DB.connections.abstracs.DBRestore
import co.japl.android.finances.services.dto.CreditCardSettingDB
import co.japl.android.finances.services.interfaces.IConnectDB
import co.japl.android.finances.services.mapping.CreditCardSettingMap
import co.japl.android.finances.services.queries.CreditCardSettingQuery
import co.japl.android.finances.services.utils.DatabaseConstants

class CreditCardSettingConnectDB: DBRestore(), IConnectDB{

    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(this.javaClass.name,"<<<=== CreditCardSetting#onCreate - Start")
        db?.execSQL(CreditCardSettingQuery.SQL_CREATE_ENTRIES)
        Log.i(this.javaClass.name,"<<<=== CreditCardSetting#onCreate - End")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== CreditCardSetting#onUpgrade - Start $oldVersion - $newVersion")
        if(oldVersion < DatabaseConstants.DATA_BASE_VERSION_MINUS_SETTING) {
            db?.execSQL(CreditCardSettingQuery.SQL_DELETE_ENTRIES)
            onCreate(db)
        }
        Log.i(this.javaClass.name,"<<<=== CreditCardSetting#onUpgrade - End")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== CreditCardSetting#onDowngrade - Start $oldVersion - $newVersion")
        db?.execSQL(CreditCardSettingQuery.SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.i(this.javaClass.name,"<<<=== CreditCardSetting#onDowngrade - End")
    }

    override fun onRestore(currentDB: SQLiteDatabase?, fromRestoreDB: SQLiteDatabase?) {
        onRestore(currentDB,fromRestoreDB,javaClass.simpleName,CreditCardSettingDB.CreditCardEntry.TABLE_NAME,CreditCardSettingMap()::restore)
    }

}