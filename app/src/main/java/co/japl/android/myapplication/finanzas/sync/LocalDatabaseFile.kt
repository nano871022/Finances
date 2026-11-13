package co.japl.android.myapplication.finanzas.sync

import java.io.File

object LocalDatabaseFile {

    fun modifiedTime(dbFile: File): Long {
        return listOf(
            dbFile,
            File("${dbFile.path}-wal"),
            File("${dbFile.path}-shm")
        ).filter { it.exists() }
            .maxOfOrNull { it.lastModified() }
            ?: 0L
    }
}