package co.japl.android.myapplication.finanzas.bussiness.impl

import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.bussiness.interfaces.ITagQuoteCreditCardSvc
import co.japl.android.myapplication.bussiness.interfaces.ITagSvc
import co.japl.android.myapplication.bussiness.mapping.TagQuoteCreditCardMap
import co.japl.android.myapplication.finanzas.bussiness.DTO.TagDTO
import co.japl.android.myapplication.finanzas.bussiness.DTO.TagsQuoteCreditCardDB
import co.japl.android.myapplication.finanzas.bussiness.DTO.TagsQuoteCreditCardDTO
import dagger.hilt.android.HiltAndroidApp
import java.util.Optional
import javax.inject.Inject

class TagQuoteCreditCardImpl @Inject constructor(override var dbConnect: SQLiteOpenHelper) : ITagQuoteCreditCardSvc {
    val COLUMNS = arrayOf(
            BaseColumns._ID,
            TagsQuoteCreditCardDB.Entry.COLUMN_CODE_TAG,
            TagsQuoteCreditCardDB.Entry.COLUMN_CODE_QUOTE_CREDIT_CARD,
            TagsQuoteCreditCardDB.Entry.COLUMN_DATE_CREATE,
            TagsQuoteCreditCardDB.Entry.COLUMN_ACTIVE
    )

    private val tagSvc:ITagSvc = TagsImpl(dbConnect)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getTags(codQuoteCreditCard: Int): List<TagDTO> {
        val db = dbConnect.readableDatabase
        val cursor = db.query(TagsQuoteCreditCardDB.Entry.TABLE_NAME
            , COLUMNS
            , "${TagsQuoteCreditCardDB.Entry.COLUMN_CODE_QUOTE_CREDIT_CARD} = ?"
            , arrayOf(codQuoteCreditCard.toString()), null, null, null)
        val mapper = TagQuoteCreditCardMap()
        val list = mutableListOf<TagDTO>()
        with(cursor) {
            while (moveToNext()) {
                mapper.mapping(this).let {
                    tagSvc.get(it.codTag).takeIf { it.isPresent }?.let {
                        list.add(it.get())
                    }
                }
            }
        }
        return list
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun save(dto: TagsQuoteCreditCardDTO): Long {
        val values = TagQuoteCreditCardMap().mapping(dto)
        val db = dbConnect.writableDatabase
        if(dto.id > 0){
            return db.update(TagsQuoteCreditCardDB.Entry.TABLE_NAME,values,"${BaseColumns._ID}=?",
                arrayOf(dto.id.toString())).toLong()
        }
        return db.insert(TagsQuoteCreditCardDB.Entry.TABLE_NAME, null, values)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAll(): List<TagsQuoteCreditCardDTO> {
        val db = dbConnect.readableDatabase
        val cursor = db.query(
            TagsQuoteCreditCardDB.Entry.TABLE_NAME,
            COLUMNS,
            null,
            null,
            null,
            null,
            null
        )
        val mapper = TagQuoteCreditCardMap()
        val list = mutableListOf<TagsQuoteCreditCardDTO>()
        with(cursor) {
            while (moveToNext()) {
                mapper.mapping(this).let {
                    list.add(it)
                }
            }
        }
        return list
    }

    override fun delete(id: Int): Boolean {
        val db = dbConnect.writableDatabase
        return db.delete(TagsQuoteCreditCardDB.Entry.TABLE_NAME, "${BaseColumns._ID}=?", arrayOf(id.toString())) > 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(id: Int): Optional<TagsQuoteCreditCardDTO> {
        val db = dbConnect.readableDatabase
        val cursor = db.query(
            TagsQuoteCreditCardDB.Entry.TABLE_NAME,
            COLUMNS,
            "${TagsQuoteCreditCardDB.Entry.COLUMN_CODE_QUOTE_CREDIT_CARD} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        val mapper = TagQuoteCreditCardMap()
        with(cursor) {
            while (moveToNext()) {
                return Optional.ofNullable(mapper.mapping(this))
            }
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