package co.japl.android.finances.services.interfaces

import android.database.sqlite.SQLiteOpenHelper
import java.util.*

interface SaveSvc<T>  {

    var dbConnect: SQLiteOpenHelper

    fun save(dto:T):Long

    fun getAll():List<T>

    fun delete(id:Int):Boolean

    fun get(id:Int):Optional<T>

    fun backup(path:String)

    fun restoreBackup(path:String)
}