package co.japl.android.finanzas.bussiness.interfaces

import android.database.sqlite.SQLiteOpenHelper
import java.util.*

interface SaveSvc<T>  {
    var dbConnect: SQLiteOpenHelper

    fun save(dto:T):Boolean

    fun getAll():List<T>

    fun delete(id:Int):Boolean

    fun get(id:Int):Optional<T>
}