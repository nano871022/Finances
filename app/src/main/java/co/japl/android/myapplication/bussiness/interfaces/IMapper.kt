package co.japl.android.myapplication.bussiness.interfaces

import android.content.ContentValues
import android.database.Cursor

interface IMapper<T> {
    fun mapping(dto:T): ContentValues
    fun mapping(cursor:Cursor): T
}