package co.japl.android.myapplication.bussiness.interfaces

import android.database.sqlite.SQLiteOpenHelper
import java.util.*

interface SaveSvc<T>  {

    var dbConnect: SQLiteOpenHelper

    fun save(dto:T):Boolean

    fun getAll():List<T>

    fun delete(id:Int):Boolean

    fun get(id:Int):Optional<T>

    fun backup(path:String)

    fun restoreBackup(path:String)
}