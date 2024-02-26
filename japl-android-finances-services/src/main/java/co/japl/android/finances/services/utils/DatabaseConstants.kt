package co.japl.android.finances.services.utils

import android.provider.BaseColumns

object DatabaseConstants {
    const val DATA_BASE_NAME = "finances.db"
    const val DATA_BASE_VERSION = 31
    const val SQL_DELETE_CALC_ID = "${BaseColumns._ID} = ?"
    const val DATA_BASE_VERSION_MINUS = 15
    const val DATA_BASE_VERSION_MINUS_SETTING = 32



}