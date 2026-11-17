package co.japl.finances.core.adapters.inbound.implement.common

import co.com.japl.finances.iports.dtos.SyncIcon
import co.com.japl.finances.iports.dtos.SyncState
import co.com.japl.finances.iports.dtos.SyncStatus
import co.com.japl.finances.iports.enums.SyncOperation
import co.com.japl.finances.iports.outbounds.IGoogleDrivePort
import co.com.japl.finances.iports.outbounds.ISyncHistoryPort
import co.com.japl.finances.iports.pojo.BackupStorageInfo
import co.com.japl.finances.iports.pojo.SyncHistory
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GoogleDriveServiceImplTest {

    private class FakeDrivePort(
        var backupResult: String? = "1234",
        var restoreResult: String? = "== Finished Restore Process",
        var version: String? = "7",
        var versionError: Exception? = null
    ) : IGoogleDrivePort {
        override suspend fun stats(): List<Pair<String, Long>> = emptyList()

        override suspend fun restore(account: GoogleSignInAccount?): String? = restoreResult

        override suspend fun backup(account: GoogleSignInAccount?): String? = backupResult

        override suspend fun infoBackup(account: GoogleSignInAccount?): String = ""

        override suspend fun getStorageInfo(account: GoogleSignInAccount?): BackupStorageInfo? = null

        override suspend fun getBackupModifiedTime(account: GoogleSignInAccount?): Long? = null

        override suspend fun getBackupVersion(account: GoogleSignInAccount?): String? {
            versionError?.let { throw it }
            return version
        }
    }

    private class FakeHistoryPort(
        var history: SyncHistory = SyncHistory(SyncOperation.NONE, null)
    ) : ISyncHistoryPort {
        override fun read(): SyncHistory = history

        override fun save(history: SyncHistory) {
            this.history = history
        }
    }

    @Test
    fun when_backup_succeeds_then_saves_backup_and_version() = runBlocking {
        val drive = FakeDrivePort(backupResult = "1234", version = "9")
        val history = FakeHistoryPort()
        val service = GoogleDriveServiceImpl(drive, history)

        val result = service.backup(null)

        assertEquals("1234", result)
        assertEquals(SyncHistory(SyncOperation.BACKUP, "9"), history.history)
    }

    @Test
    fun when_backup_returns_zero_then_does_not_save_history() = runBlocking {
        val drive = FakeDrivePort(backupResult = "0", version = "9")
        val history = FakeHistoryPort()
        val service = GoogleDriveServiceImpl(drive, history)

        service.backup(null)

        assertEquals(SyncHistory(SyncOperation.NONE, null), history.history)
    }

    @Test
    fun when_backup_returns_null_then_does_not_save_history() = runBlocking {
        val drive = FakeDrivePort(backupResult = null, version = "9")
        val history = FakeHistoryPort()
        val service = GoogleDriveServiceImpl(drive, history)

        service.backup(null)

        assertEquals(SyncHistory(SyncOperation.NONE, null), history.history)
    }

    @Test
    fun when_restore_succeeds_then_saves_restore_and_version() = runBlocking {
        val drive = FakeDrivePort(restoreResult = "== Finished Restore Process", version = "9")
        val history = FakeHistoryPort()
        val service = GoogleDriveServiceImpl(drive, history)

        val result = service.restore(null)

        assertEquals("== Finished Restore Process", result)
        assertEquals(SyncHistory(SyncOperation.RESTORE, "9"), history.history)
    }

    @Test
    fun when_restore_returns_null_then_does_not_save_history() = runBlocking {
        val drive = FakeDrivePort(restoreResult = null, version = "9")
        val history = FakeHistoryPort()
        val service = GoogleDriveServiceImpl(drive, history)

        service.restore(null)

        assertEquals(SyncHistory(SyncOperation.NONE, null), history.history)
    }

    @Test
    fun when_version_read_fails_after_backup_then_history_is_unchanged() = runBlocking {
        val drive = FakeDrivePort(
            backupResult = "1234",
            versionError = IllegalStateException("network")
        )
        val history = FakeHistoryPort()
        val service = GoogleDriveServiceImpl(drive, history)

        val result = service.backup(null)

        assertEquals("1234", result)
        assertEquals(SyncHistory(SyncOperation.NONE, null), history.history)
    }

    @Test
    fun when_sync_status_versions_match_then_updated() = runBlocking {
        val drive = FakeDrivePort(version = "7")
        val history = FakeHistoryPort(SyncHistory(SyncOperation.BACKUP, "7"))
        val service = GoogleDriveServiceImpl(drive, history)

        val result = service.getSyncStatus(null, autoSyncEnabled = true, isMaster = true)

        assertEquals(SyncStatus(SyncIcon.UPLOAD, SyncState.UPDATED), result)
    }

    @Test
    fun when_sync_status_versions_differ_then_outdated() = runBlocking {
        val drive = FakeDrivePort(version = "8")
        val history = FakeHistoryPort(SyncHistory(SyncOperation.RESTORE, "7"))
        val service = GoogleDriveServiceImpl(drive, history)

        val result = service.getSyncStatus(null, autoSyncEnabled = true, isMaster = false)

        assertEquals(SyncStatus(SyncIcon.DOWNLOAD, SyncState.OUTDATED), result)
    }

    @Test
    fun when_sync_status_check_fails_then_unknown() = runBlocking {
        val drive = FakeDrivePort(versionError = IllegalStateException("network"))
        val history = FakeHistoryPort(SyncHistory(SyncOperation.BACKUP, "7"))
        val service = GoogleDriveServiceImpl(drive, history)

        val result = service.getSyncStatus(null, autoSyncEnabled = false, isMaster = true)

        assertEquals(SyncStatus(SyncIcon.UPLOAD, SyncState.UNKNOWN), result)
    }

    @Test
    fun when_sync_status_has_no_history_then_not_synced() = runBlocking {
        val drive = FakeDrivePort(version = "7")
        val history = FakeHistoryPort()
        val service = GoogleDriveServiceImpl(drive, history)

        val result = service.getSyncStatus(null, autoSyncEnabled = false, isMaster = false)

        assertEquals(SyncStatus(SyncIcon.SYNC, SyncState.NOT_SYNCED), result)
    }
}
