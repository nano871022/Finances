package co.japl.android.finances.services.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.finances.services.DB.connections.abstracs.DBRestore
import co.japl.android.finances.services.dto.TaxDB
import co.japl.android.finances.services.interfaces.IConnectDB
import co.japl.android.finances.services.mapping.TaxMap
import co.japl.android.finances.services.queries.TaxQuery
import co.japl.android.finances.services.utils.DatabaseConstants
import java.lang.Exception

class TaxConnectDB: DBRestore(), IConnectDB{

    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(this.javaClass.name,"<<<=== TaxConnectDB#onCreate - Start")
        db?.execSQL(TaxQuery.SQL_CREATE_ENTRIES)
        try {
            db?.execSQL("ALTER TABLE ${TaxDB.TaxEntry.TABLE_NAME} ADD COLUMN ${TaxDB.TaxEntry.COLUMN_CASH_ADVANCE_VALUE} DOUBLE")
        } catch (e: Exception) {
            Log.e(this.javaClass.name, "Error adding column cash advance value in onCreate: ${e.message}")
        }
        Log.i(this.javaClass.name,"<<<=== TaxConnectDB#onCreate - End")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== TaxConnectDB#onUpgrade - Start $oldVersion - $newVersion")
        if(oldVersion < DatabaseConstants.DATA_BASE_VERSION_MINUS) {
            db?.execSQL(TaxQuery.SQL_DELETE_ENTRIES)
            onCreate(db)
        }else{
            var findVersion = oldVersion+1
            while(findVersion <= newVersion) {
                try {
                    val value = findVersion.toString()
                    if(TaxQuery.SQL_ALTER.containsKey(value)) {
                        val query = TaxQuery.SQL_ALTER[value]
                        Log.d(this.javaClass.name, "Version $value query $query")
                        db?.execSQL(query)
                    }
                }catch (e:Exception){
                    Log.e(this.javaClass.name,"Exception $e continue $findVersion")
                }finally {
                    findVersion++
                }
            }
        }
        try {
            db?.execSQL("ALTER TABLE ${TaxDB.TaxEntry.TABLE_NAME} ADD COLUMN ${TaxDB.TaxEntry.COLUMN_CASH_ADVANCE_VALUE} DOUBLE")
        } catch (e: Exception) {
            Log.e(this.javaClass.name, "Error adding column cash advance value in onUpgrade: ${e.message}")
        }
        Log.i(this.javaClass.name,"<<<=== TaxConnectDB#onUpgrade - End")
    }

    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
        try {
            db?.execSQL("ALTER TABLE ${TaxDB.TaxEntry.TABLE_NAME} ADD COLUMN ${TaxDB.TaxEntry.COLUMN_CASH_ADVANCE_VALUE} DOUBLE")
        } catch (e: Exception) {
            // Ignored if column already exists
        }
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== TaxConnectDB#onDowngrade - Start $oldVersion - $newVersion")
        db?.execSQL(TaxQuery.SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.i(this.javaClass.name,"<<<=== TaxConnectDB#onDowngrade - End")
    }

    override fun onRestore(currentDB: SQLiteDatabase?, fromRestoreDB: SQLiteDatabase?) {
        onRestore(currentDB,fromRestoreDB,javaClass.simpleName,TaxDB.TaxEntry.TABLE_NAME,TaxMap()::restore)
    }

    override fun onStats(connectionDB: SQLiteDatabase?): Pair<String, Long> {
        return onStats(connectionDB, TaxDB.TaxEntry.TABLE_NAME)
    }

}
