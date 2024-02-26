package co.japl.android.myapplication.finanzas.bussiness.mapping

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.finanzas.bussiness.DTO.*
import co.com.japl.ui.utils.DateUtils

class DifferInstallmentMap {

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(cursor: Cursor):DifferInstallmentDTO{
        val id  = cursor.getInt(0)
        val date = DateUtils.toLocalDate(cursor.getString(1))
        val cdBoughtCreditCard = cursor.getLong(2)
        val pendingValuePayable = cursor.getDouble(3)
        val originValue = cursor.getDouble(4)
        val newInstallment = cursor.getDouble(5)
        val oldInstallment = cursor.getDouble(6)
        return DifferInstallmentDTO(id,date, cdBoughtCreditCard, pendingValuePayable , originValue, newInstallment, oldInstallment)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapping(dto: DifferInstallmentDTO): ContentValues {
        return ContentValues().apply {
            put(DifferInstallmentDB.Entry.COLUMN_CODE_QUOTE,dto.cdBoughtCreditCard)
            put(DifferInstallmentDB.Entry.COLUMN_PENDING_VALUE_PAYABLE,dto.pendingValuePayable)
            put(DifferInstallmentDB.Entry.COLUMN_ORIGIN_VALUE,dto.originValue)
            put(DifferInstallmentDB.Entry.COLUMN_NEW_INSTALLMENT,dto.newInstallment)
            put(DifferInstallmentDB.Entry.COLUMN_OLD_INSTALLMENT, dto.oldInstallment)
            put(DifferInstallmentDB.Entry.COLUMN_DATE_CREATE, DateUtils.localDateToStringDate(dto.create))
        }
    }
}