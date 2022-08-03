package co.japl.android.myapplication.utils

import android.provider.BaseColumns

object DatabaseConstants {
    const val DATA_BASE_NAME = "finances.db"
    const val DATA_BASE_VERSION = 15
    const val SQL_DELETE_CALC_ID = "${BaseColumns._ID} = ?"




}