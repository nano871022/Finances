package co.japl.android.finanzas.utils

import android.provider.BaseColumns
import co.japl.android.finanzas.bussiness.DTO.CalcDB
import co.japl.android.finanzas.bussiness.DTO.CreditCardBoughtDB

object DatabaseConstants {
    const val DATA_BASE_NAME = "finances.db"
    const val DATA_BASE_VERSION = 15
    const val SQL_DELETE_CALC_ID = "${BaseColumns._ID} = ?"




}