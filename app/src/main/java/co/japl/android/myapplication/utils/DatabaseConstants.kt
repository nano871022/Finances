package co.japl.android.myapplication.utils

import android.provider.BaseColumns
import co.japl.android.myapplication.bussiness.DTO.CalcDB
import co.japl.android.myapplication.bussiness.DTO.CreditCardBoughtDB

object DatabaseConstants {
    const val DATA_BASE_NAME = "finances.db"
    const val DATA_BASE_VERSION = 14
    const val SQL_DELETE_CALC_ID = "${BaseColumns._ID} = ?"




}