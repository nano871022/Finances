package co.japl.android.finances.services.DB.connections

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.finances.services.interfaces.IConnectDB
import co.japl.android.finances.services.queries.TagsQuery
import java.lang.Exception

class TagConnectDB:IConnectDB{

    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(this.javaClass.name,"<<<=== TagConnectDB#onCreate - Start")
        db?.execSQL(TagsQuery.SQL_CREATE_ENTRIES)
        Log.i(this.javaClass.name,"<<<=== TagConnectDB#onCreate - End")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== TagConnectDB#onUpgrade - Start $oldVersion - $newVersion")
        if(oldVersion < TagsQuery.DATA_BASE_VERSION_MINUS) {
            db?.execSQL(TagsQuery.SQL_DELETE_ENTRIES)
            onCreate(db)
        }else{
            var findVersion = oldVersion+1
            while(findVersion <= newVersion) {
                try {
                    val value = findVersion.toString()
                    if(TagsQuery.SQL_ALTER.containsKey(value)) {
                        val query = TagsQuery.SQL_ALTER[value]
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
        Log.i(this.javaClass.name,"<<<=== TagConnectDB#onUpgrade - End")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i(this.javaClass.name,"<<<=== TagConnectDB#onDowngrade - Start $oldVersion - $newVersion")
        db?.execSQL(TagsQuery.SQL_DELETE_ENTRIES)
        onCreate(db)
        Log.i(this.javaClass.name,"<<<=== TagConnectDB#onDowngrade - End")
    }

}