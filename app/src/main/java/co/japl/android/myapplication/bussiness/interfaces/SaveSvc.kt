package co.japl.android.myapplication.bussiness.interfaces

import android.database.sqlite.SQLiteOpenHelper

interface SaveSvc<T>  {
    var dbConnect: SQLiteOpenHelper

    fun save(dto:T):Boolean

    fun getAll():List<T>

    fun delete(id:Int):Boolean
}