package co.japl.android.myapplication.bussiness.interfaces

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.myapplication.bussiness.queries.TaxQuery
import java.lang.Exception

interface IConnectDB {
    fun onCreate(db: SQLiteDatabase?)
    fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int)
    fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int)

    fun update(oldVersion:Int,newVersion:Int,sqlAlter:Map<String,String>,fn:(sql:String)->Unit) {
        var findVersion = oldVersion+1
        while(findVersion <= newVersion) {
            try {
                val value = findVersion.toString()
                if(sqlAlter.containsKey(value)) {
                    sqlAlter[value]?.let{
                        fn.invoke(it)
                        Log.d(this.javaClass.name, "Version $value query $it")
                    }
                }
            }catch (e: Exception){
                Log.e(this.javaClass.name,"Exception $e continue $findVersion")
            }finally {
                findVersion++
            }
        }
    }
}