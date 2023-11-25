package co.japl.android.finances.services.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.finances.services.interfaces.IConnectDB
import co.japl.android.finances.services.queries.TagsQuoteCreditCardQuery
import java.lang.Exception

class TagQuoteCreditCardConnectDB:IConnectDB{

    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(this.javaClass.name,"<<<=== TagQuoteCreditCardConnectDB#onCreate - Start")
        db?.execSQL(TagsQuoteCreditCardQuery.SQL_CREATE_ENTRIES)
        Log.i(this.javaClass.name,"<<<=== TagQuoteCreditCardConnectDB#onCreate - End")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== TagQuoteCreditCardConnectDB#onUpgrade - Start $oldVersion - $newVersion")
        if(oldVersion < TagsQuoteCreditCardQuery.DATA_BASE_VERSION_MINUS) {
            db?.execSQL(TagsQuoteCreditCardQuery.SQL_DELETE_ENTRIES)
            onCreate(db)
        }else{
            var findVersion = oldVersion+1
            while(findVersion <= newVersion) {
                try {
                    val value = findVersion.toString()
                    if(TagsQuoteCreditCardQuery.SQL_ALTER.containsKey(value)) {
                        val query = TagsQuoteCreditCardQuery.SQL_ALTER[value]
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
        Log.i(this.javaClass.name,"<<<=== TagQuoteCreditCardConnectDB#onUpgrade - End")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== TagQuoteCreditCardConnectDB#onDowngrade - Start $oldVersion - $newVersion")
        db?.execSQL(TagsQuoteCreditCardQuery.SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.i(this.javaClass.name,"<<<=== TagQuoteCreditCardConnectDB#onDowngrade - End")
    }

}