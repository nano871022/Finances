package com.nano871022.finances.service.adapter.outbound.database

import android.database.sqlite.SQLiteDatabase
import co.japl.android.finances.services.interfaces.IConnectDB

class DianTaxConnectDB : IConnectDB {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("""
            CREATE TABLE dian_manual_patrimony_inputs (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                value TEXT NOT NULL,
                type TEXT NOT NULL
            )
        """.trimIndent())

        db?.execSQL("""
            CREATE TABLE dian_tax_declarations_history (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                fiscal_year INTEGER NOT NULL,
                tax_value_cop TEXT NOT NULL,
                date TEXT NOT NULL
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
    override fun onRestore(currentDB: SQLiteDatabase?, fromRestoreDB: SQLiteDatabase?) {}
    override fun onStats(connectionDB: SQLiteDatabase?): Pair<String, Long> = "DianTax" to 0L
}
