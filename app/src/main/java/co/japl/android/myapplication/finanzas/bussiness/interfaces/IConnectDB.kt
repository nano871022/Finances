package co.japl.android.myapplication.bussiness.interfaces

import android.database.sqlite.SQLiteDatabase

interface IConnectDB {
    fun onCreate(db: SQLiteDatabase?)
    fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int)
    fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int)
}