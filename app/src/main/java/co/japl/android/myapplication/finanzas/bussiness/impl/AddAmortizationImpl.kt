package co.japl.android.myapplication.finanzas.bussiness.impl

import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.finanzas.bussiness.DTO.AddAmortizationDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.AddAmortizationDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.AdditionalCreditDB
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IAddAmortizationSvc
import co.japl.android.myapplication.finanzas.bussiness.mapping.AddAmortizationMap
import java.time.LocalDate
import java.util.Optional
import javax.inject.Inject

class AddAmortizationImpl @Inject constructor(override var dbConnect: SQLiteOpenHelper) : IAddAmortizationSvc {
    val COLUMNS = arrayOf(
        BaseColumns._ID,
        AddAmortizationDB.Entry.COLUMN_CODE,
        AddAmortizationDB.Entry.COLUMN_NBR_QUOTE,
        AddAmortizationDB.Entry.COLUMN_VALUE,
        AddAmortizationDB.Entry.COLUMN_DATE_CREATE,
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun createNew(code: Int, nbrQuote: Long, value: Double): Boolean {
        val dto = AddAmortizationDTO(
            0,
            LocalDate.now(),
            code,
            nbrQuote,
            value
        )
        return save(dto) > 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAll(code: Int): List<AddAmortizationDTO> {
        val db = dbConnect.readableDatabase
        val cursor = db.query(
            AddAmortizationDB.Entry.TABLE_NAME,
            COLUMNS,
            "${AddAmortizationDB.Entry.COLUMN_CODE}=?",
            arrayOf(code.toString()),
            null,
            null,
            null
        )
        val list = mutableListOf<AddAmortizationDTO>()
        while (cursor.moveToNext()){
            list.add(AddAmortizationMap().mapping(cursor))
        }
        return list.also { Log.d(javaClass.name, "<<<=== FINISH::getAll Code: $code Response: $list") }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun save(dto: AddAmortizationDTO): Long {
        val db = dbConnect.writableDatabase
        val values = AddAmortizationMap().mapping(dto)
        return db.insert(AddAmortizationDB.Entry.TABLE_NAME, null, values)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAll(): List<AddAmortizationDTO> {
        val db = dbConnect.readableDatabase
        val cursor = db.query(
            AddAmortizationDB.Entry.TABLE_NAME,
            COLUMNS,
            null,
            null,
            null,
            null,
            null
        )
        val list = mutableListOf<AddAmortizationDTO>()
        while (cursor.moveToNext()) {
            list.add(AddAmortizationMap().mapping(cursor))
        }
        return list.also { Log.d(javaClass.name, "getAll: $list") }
    }

    override fun delete(id: Int): Boolean {
        val db = dbConnect.readableDatabase
        return db.delete(AddAmortizationDB.Entry.TABLE_NAME, "${BaseColumns._ID}=?", arrayOf(id.toString())) > 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(id: Int): Optional<AddAmortizationDTO> {
        val db = dbConnect.readableDatabase
        val cursor = db.query(
            AddAmortizationDB.Entry.TABLE_NAME,
            COLUMNS,
            "${BaseColumns._ID}=?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        while (cursor.moveToNext()){
            return Optional.of(AddAmortizationMap().mapping(cursor))
        }
        return Optional.empty()
    }

    override fun backup(path: String) {
        TODO("Not yet implemented")
    }

    override fun restoreBackup(path: String) {
        TODO("Not yet implemented")
    }
}