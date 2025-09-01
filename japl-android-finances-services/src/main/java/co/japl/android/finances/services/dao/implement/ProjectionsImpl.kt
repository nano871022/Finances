package co.japl.android.finances.services.dao.implement

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import androidx.annotation.RequiresApi
import co.japl.android.finances.services.dao.interfaces.IProjectionsSvc
import co.japl.android.finances.services.dto.ProjectionDB
import co.japl.android.finances.services.dto.ProjectionDTO
import co.japl.android.finances.services.mapping.ProjectionMap
import co.japl.android.finances.services.utils.DatabaseConstants
import java.util.Optional
import javax.inject.Inject

class ProjectionsImpl @Inject constructor(override var dbConnect: SQLiteOpenHelper) :
    IProjectionsSvc {
    val COLUMNS = arrayOf(
        BaseColumns._ID,
        ProjectionDB.Entry.COLUMN_NAME,
        ProjectionDB.Entry.COLUMN_VALUE,
        ProjectionDB.Entry.COLUMN_TYPE,
        ProjectionDB.Entry.COLUMN_ACTIVE,
        ProjectionDB.Entry.COLUMN_QUOTE,
        ProjectionDB.Entry.COLUMN_DATE_CREATE,
        ProjectionDB.Entry.COLUMN_DATE_END
    )

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAllActive(): List<ProjectionDTO> {
        val db = dbConnect.readableDatabase
        val items = mutableListOf<ProjectionDTO>()
        val mapper = ProjectionMap()
        db.query(
            ProjectionDB.Entry.TABLE_NAME, COLUMNS, "${ProjectionDB.Entry.COLUMN_ACTIVE}=1",
            null, null, null, "${ProjectionDB.Entry.COLUMN_DATE_END} ASC"
        )
            ?.use { cursor ->
                with(cursor) {
                    while (moveToNext()) {
                        items.add(mapper.mapping(cursor))
                    }
                }
            }
        items.sortBy { it.end }
        return items
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun save(dto: ProjectionDTO): Long {
        val db = dbConnect.writableDatabase
        val content: ContentValues? = ProjectionMap().mapping(dto)
        return if (dto.id > 0) {
            db?.update(
                ProjectionDB.Entry.TABLE_NAME,
                content,
                "${BaseColumns._ID}=?",
                arrayOf(dto.id.toString())
            )?.toLong() ?: 0
        } else {
            db?.insert(ProjectionDB.Entry.TABLE_NAME, null, content) ?: 0
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getAll(): List<ProjectionDTO> {
        val db = dbConnect.readableDatabase
        val items = mutableListOf<ProjectionDTO>()
        val mapper = ProjectionMap()
        db.query(ProjectionDB.Entry.TABLE_NAME, COLUMNS, null, null, null, null, null)
            ?.use { cursor ->
                with(cursor) {
                    while (moveToNext()) {
                        items.add(mapper.mapping(cursor))
                    }
                }
            }
        return items
    }

    override fun delete(id: Int): Boolean {
        val db = dbConnect.writableDatabase
        return db.delete(
            ProjectionDB.Entry.TABLE_NAME, DatabaseConstants.SQL_DELETE_CALC_ID,
            arrayOf(id.toString())
        ) > 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(id: Int): Optional<ProjectionDTO> {
        val db = dbConnect.readableDatabase

        val mapper = ProjectionMap()
        db.query(
            ProjectionDB.Entry.TABLE_NAME, COLUMNS, "${BaseColumns._ID} = ?",
            arrayOf(id.toString()), null, null, null
        )?.use { cursor ->
            with(cursor) {
                while (moveToNext()) {
                    return@get Optional.ofNullable(mapper.mapping(cursor))
                }
            }
        }
        return Optional.empty()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun get(values: ProjectionDTO): List<ProjectionDTO> {
        val db = dbConnect.readableDatabase
        val items = mutableListOf<ProjectionDTO>()
        val mapper = ProjectionMap()
        db.query(ProjectionDB.Entry.TABLE_NAME, COLUMNS, null, null, null, null, null)
            ?.use { cursor ->
                with(cursor) {
                    while (moveToNext()) {
                        items.add(mapper.mapping(cursor))
                    }
                }
            }
        return items
    }

    override fun backup(path: String) {
    }

    override fun restoreBackup(path: String) {
    }
}