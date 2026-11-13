package co.japl.android.myapplication.finanzas.sync

import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class LocalDatabaseFileTest {

    @get:Rule
    val folder = TemporaryFolder()

    @Test
    fun when_no_database_file_exists_then_zero_is_returned() {
        val db = File(folder.root, "finances.db")

        assertEquals(0L, LocalDatabaseFile.modifiedTime(db))
    }

    @Test
    fun when_only_the_database_exists_then_its_time_is_returned() {
        val db = folder.newFile("finances.db")
        db.setLastModified(1_000L)

        assertEquals(1_000L, LocalDatabaseFile.modifiedTime(db))
    }

    @Test
    fun when_the_wal_is_newer_then_the_wal_time_is_returned() {
        val db = folder.newFile("finances.db")
        db.setLastModified(1_000L)
        val wal = folder.newFile("finances.db-wal")
        wal.setLastModified(2_000L)

        assertEquals(2_000L, LocalDatabaseFile.modifiedTime(db))
    }

    @Test
    fun when_the_shm_is_newer_then_the_shm_time_is_returned() {
        val db = folder.newFile("finances.db")
        db.setLastModified(1_000L)
        val wal = folder.newFile("finances.db-wal")
        wal.setLastModified(2_000L)
        val shm = folder.newFile("finances.db-shm")
        shm.setLastModified(3_000L)

        assertEquals(3_000L, LocalDatabaseFile.modifiedTime(db))
    }
}