package co.japl.android.myapplication.finanzas.bussiness.impl

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.bussiness.DTO.CreditCardBoughtDB
import co.japl.android.myapplication.bussiness.interfaces.SaveSvc
import co.japl.android.myapplication.finanzas.bussiness.DTO.AdditionalCreditDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.AdditionalCreditDTO
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IAdditionalCreditSvc
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ISaveSvc
import co.japl.android.myapplication.finanzas.bussiness.mapping.AdditionalMap
import co.japl.android.myapplication.finanzas.putParams.AdditionalCreditParams
import co.japl.android.myapplication.utils.DatabaseConstants
import co.japl.android.myapplication.utils.DateUtils
import co.japl.android.myapplication.utils.NumbersUtil
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

class AdditionalCreditImpl(override var dbConnect: SQLiteOpenHelper) : SaveSvc<AdditionalCreditDTO>,ISaveSvc<AdditionalCreditDTO> , IAdditionalCreditSvc{
    public  val COLUMNS = arrayOf(
        BaseColumns._ID,
        AdditionalCreditDB.Entry.COLUMN_NAME,
        AdditionalCreditDB.Entry.COLUMN_VALUE,
        AdditionalCreditDB.Entry.COLUMN_CREDIT_CODE,
        AdditionalCreditDB.Entry.COLUMN_START_DATE,
        AdditionalCreditDB.Entry.COLUMN_END_DATE
    )
    private val FORMAT_DATE_END_WHERE = "substr(${AdditionalCreditDB.Entry.COLUMN_END_DATE},7,4)||'-'||substr(${AdditionalCreditDB.Entry.COLUMN_END_DATE},4,2)||'-'||substr(${AdditionalCreditDB.Entry.COLUMN_END_DATE},1,2)"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun save(dto: AdditionalCreditDTO): Long {
        val db = dbConnect.writableDatabase
        val values:ContentValues? = AdditionalMap().mapping(dto)
        return (db?.insert(AdditionalCreditDB.Entry.TABLE_NAME,null,values) ?: 0).also {
            Log.d(javaClass.name,"<<<=== END SAVE Id $dto Record: $it CreditCode: ${dto.creditCode}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun update(dto:AdditionalCreditDTO):Int{
        val db = dbConnect.writableDatabase
        val values:ContentValues? = AdditionalMap().mapping(dto)
        return ((db?.update(AdditionalCreditDB.Entry.TABLE_NAME,values,"${BaseColumns._ID} = ?",arrayOf(dto.id.toString()))) ?: 0).also {
            Log.d(javaClass.name,"<<<=== END UPDATE Id $dto Record: $it CreditCode: ${dto.creditCode}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAll(): List<AdditionalCreditDTO> {
        val db = dbConnect.readableDatabase

        val cursor = db.query(AdditionalCreditDB.Entry.TABLE_NAME,COLUMNS,null,null,null,null,null)
        val items = mutableListOf<AdditionalCreditDTO>()
        val mapper = AdditionalMap()
        with(cursor){
            while(moveToNext()){
                items.add( mapper.mapping(cursor))
            }
        }
        return items
    }

    override fun delete(id: Int): Boolean {
        val db = dbConnect.writableDatabase
        return db.delete(AdditionalCreditDB.Entry.TABLE_NAME,DatabaseConstants.SQL_DELETE_CALC_ID,
            arrayOf(id.toString())) > 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(id: Int): Optional<AdditionalCreditDTO> {
        val db = dbConnect.readableDatabase

        val cursor = db.query(AdditionalCreditDB.Entry.TABLE_NAME,COLUMNS,"${AdditionalCreditDB.Entry.COLUMN_CREDIT_CODE} = ?",
            arrayOf(id.toString()),null,null,null)
        with(cursor){
            while(moveToNext()){
                return Optional.ofNullable(AdditionalMap().mapping(cursor))
            }
        }
        return Optional.empty()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAdditional(id:Int):AdditionalCreditDTO? {
        val db = dbConnect.readableDatabase
        val cursor = db.query(
            AdditionalCreditDB.Entry.TABLE_NAME,
            COLUMNS,
            "${BaseColumns._ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        with(cursor) {
            while (moveToNext()) {
                return AdditionalMap().mapping(cursor)
            }
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(values: AdditionalCreditDTO): List<AdditionalCreditDTO> {
        val db = dbConnect.readableDatabase
        val localDate = LocalDate.now()

        val cursor = db.query(AdditionalCreditDB.Entry.TABLE_NAME,COLUMNS
            ,"${AdditionalCreditDB.Entry.COLUMN_CREDIT_CODE} = ? AND $FORMAT_DATE_END_WHERE >= ?"
            , arrayOf(values.creditCode.toString(),DateUtils.localDateToStringDate(localDate)),null,null,null)
        val list = arrayListOf<AdditionalCreditDTO>()
        val mapper = AdditionalMap()
        with(cursor){
            while(moveToNext()){
                list.add( mapper.mapping(cursor) )
            }
        }
        return list.also { Log.d(javaClass.name,"<<<=== END Get $it CreditCard: ${values.creditCode}") }
    }

    override fun backup(path: String) {
    }

    override fun restoreBackup(path: String) {
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun updateValue(id: Int, value: BigDecimal): Boolean {
        Log.d(javaClass.name,"<<<=== UpdateValue Id $id Value $value")
        val dto = getAdditional(id)
        return dto?.let { dto->
            val dtoNew = dto.copy()
            dtoNew.endDate = LocalDate.of(9999, 12, 31)
            dtoNew.startDate = dto.startDate.withMonth(LocalDate.now().monthValue).withYear(
                LocalDate.now().year
            )
            dtoNew.id = 0
            dtoNew.value = value
            Log.d(javaClass.name,"Before call create new value")
            val response = save(dtoNew)
            if(response > 0) {
                dto.endDate = dto.startDate.minusMonths(1)
                return update(dto) > 0
            }
            return false
        } ?: false
    }
}