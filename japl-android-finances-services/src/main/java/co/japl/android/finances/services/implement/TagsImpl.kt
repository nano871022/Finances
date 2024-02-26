package co.japl.android.finances.services.implement

import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import android.util.Log
import androidx.annotation.RequiresApi
import co.japl.android.finances.services.interfaces.ITagSvc
import co.japl.android.finances.services.mapping.TagMap
import co.japl.android.finances.services.dto.TagDB
import co.japl.android.finances.services.dto.TagDTO
import java.util.Optional
import javax.inject.Inject


class TagsImpl @Inject constructor(override var dbConnect: SQLiteOpenHelper): ITagSvc{
    private val COLUMNS = arrayOf(
                BaseColumns._ID
                ,TagDB.Entry.COLUMN_NAME
                ,TagDB.Entry.COLUMN_DATE_CREATE
                ,TagDB.Entry.COLUMN_ACTIVE)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun save(dto: TagDTO): Long {
        requireNotNull(dto.name)
        require(dto.name.isNotBlank())
        Log.d(javaClass.name,"<<<=== START::Save $dto")
        val values = TagMap().mapping(dto)
        val dbConnect = dbConnect.writableDatabase
        return dbConnect.insert(TagDB.Entry.TABLE_NAME, null, values).also {
            Log.d(javaClass.name,"<<<=== FINISH::Save $it")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAll(): List<TagDTO> {
        val dbConnect = dbConnect.readableDatabase
        val cursor = dbConnect.query(TagDB.Entry.TABLE_NAME,COLUMNS,null,null,null,null,null)
        val list = mutableListOf<TagDTO>()
        val mapper = TagMap()
        with(cursor){
            while(moveToNext()){
                list.add(mapper.mapping(this))
            }
        }
        return list
    }

    override fun delete(id: Int): Boolean {
        val dbConnect = dbConnect.writableDatabase
        return dbConnect.delete(TagDB.Entry.TABLE_NAME, "${BaseColumns._ID}=?", arrayOf(id.toString())) > 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(id: Int): Optional<TagDTO> {
        val dbConnect = dbConnect.readableDatabase
        val cursor = dbConnect.query(TagDB.Entry.TABLE_NAME,COLUMNS,"${BaseColumns._ID} = ?",
            arrayOf(id.toString()),null,null,null)
        val mapper = TagMap()
        with(cursor){
            while(moveToNext()){
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