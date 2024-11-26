package co.japl.android.finances.services.interfaces

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.finances.services.queries.TaxQuery
import java.lang.Exception

interface IConnectDB {
    fun onCreate(db: SQLiteDatabase?)
    fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int)
    fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int)

    fun onRestore(currentDB: SQLiteDatabase?, fromRestoreDB: SQLiteDatabase?)

    fun updateRevert(){

    }
    fun update(oldVersion:Int,newVersion:Int,sqlAlter:Map<String,List<String>>,fn:(sql:String)->Unit) {
        var findVersion = oldVersion+1
        while(findVersion <= newVersion) {
            try {
                val value = findVersion.toString()
                if(sqlAlter.containsKey(value)) {
                    sqlAlter[value]?.let{
                        it?.forEach {
                            fn.invoke(it)
                            Log.d(this.javaClass.name, "Version $value query $it")
                        }
                    }
                }
            }catch (e: Exception){
                Log.e(this.javaClass.name,"Exception $e continue $findVersion")
            }finally {
                findVersion++
            }
        }
    }

    fun onStats(connectionDB: SQLiteDatabase?):Pair<String,Long>
    fun onStats(connectionDB: SQLiteDatabase?,tableName:String):Pair<String,Long>{
        return connectionDB?.let {
            val cursor = it.rawQuery("SELECT COUNT(1) as cnt FROM $tableName", arrayOf())
            with(cursor) {
                while(moveToNext()) {
                    val columnPos = cursor.getColumnIndex("cnt")
                    val value = cursor.getLong(columnPos)
                    return Pair(tableName, value).also { cursor.close() }
                }
            }
            return Pair(tableName,0)
        }?:Pair(tableName,0)
    }
}