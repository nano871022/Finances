package co.japl.finances.core.usercases.calculations

import co.com.japl.finances.iports.dtos.SyncIcon
import co.com.japl.finances.iports.dtos.SyncState
import co.com.japl.finances.iports.dtos.SyncStatus
import co.com.japl.finances.iports.enums.SyncOperation
import co.com.japl.finances.iports.pojo.SyncHistory
import org.junit.Assert.assertEquals
import org.junit.Test

class SyncStatusResolverTest {

    private fun resolve(
        autoSyncEnabled: Boolean = false,
        isMaster: Boolean = false,
        operation: SyncOperation = SyncOperation.NONE,
        recordedVersion: String? = null,
        currentVersion: String? = null,
        driveChecked: Boolean = true
    ): SyncStatus = SyncStatusResolver.resolve(
        autoSyncEnabled = autoSyncEnabled,
        isMaster = isMaster,
        history = SyncHistory(operation, recordedVersion),
        currentDriveVersion = currentVersion,
        driveChecked = driveChecked
    )

    @Test
    fun when_master_automatic_and_versions_match_then_upload_and_updated() {
        assertEquals(
            SyncStatus(SyncIcon.UPLOAD, SyncState.UPDATED),
            resolve(
                autoSyncEnabled = true,
                isMaster = true,
                operation = SyncOperation.BACKUP,
                recordedVersion = "7",
                currentVersion = "7"
            )
        )
    }

    @Test
    fun when_slave_automatic_and_versions_match_then_download_and_updated() {
        assertEquals(
            SyncStatus(SyncIcon.DOWNLOAD, SyncState.UPDATED),
            resolve(
                autoSyncEnabled = true,
                isMaster = false,
                operation = SyncOperation.RESTORE,
                recordedVersion = "7",
                currentVersion = "7"
            )
        )
    }

    @Test
    fun when_master_automatic_and_versions_differ_then_upload_and_outdated() {
        assertEquals(
            SyncStatus(SyncIcon.UPLOAD, SyncState.OUTDATED),
            resolve(
                autoSyncEnabled = true,
                isMaster = true,
                operation = SyncOperation.BACKUP,
                recordedVersion = "7",
                currentVersion = "8"
            )
        )
    }

    @Test
    fun when_slave_automatic_and_versions_differ_then_download_and_outdated() {
        assertEquals(
            SyncStatus(SyncIcon.DOWNLOAD, SyncState.OUTDATED),
            resolve(
                autoSyncEnabled = true,
                isMaster = false,
                operation = SyncOperation.RESTORE,
                recordedVersion = "7",
                currentVersion = "8"
            )
        )
    }

    @Test
    fun when_automatic_disabled_and_last_operation_backup_then_upload() {
        assertEquals(
            SyncStatus(SyncIcon.UPLOAD, SyncState.UPDATED),
            resolve(
                autoSyncEnabled = false,
                operation = SyncOperation.BACKUP,
                recordedVersion = "7",
                currentVersion = "7"
            )
        )
    }

    @Test
    fun when_automatic_disabled_and_last_operation_restore_then_download() {
        assertEquals(
            SyncStatus(SyncIcon.DOWNLOAD, SyncState.UPDATED),
            resolve(
                autoSyncEnabled = false,
                operation = SyncOperation.RESTORE,
                recordedVersion = "7",
                currentVersion = "7"
            )
        )
    }

    @Test
    fun when_automatic_disabled_and_last_backup_version_differs_then_outdated() {
        assertEquals(
            SyncStatus(SyncIcon.UPLOAD, SyncState.OUTDATED),
            resolve(
                autoSyncEnabled = false,
                operation = SyncOperation.BACKUP,
                recordedVersion = "7",
                currentVersion = "9"
            )
        )
    }

    @Test
    fun when_automatic_disabled_and_last_restore_version_differs_then_outdated() {
        assertEquals(
            SyncStatus(SyncIcon.DOWNLOAD, SyncState.OUTDATED),
            resolve(
                autoSyncEnabled = false,
                operation = SyncOperation.RESTORE,
                recordedVersion = "7",
                currentVersion = "9"
            )
        )
    }

    @Test
    fun when_no_history_then_not_synced() {
        assertEquals(
            SyncStatus(SyncIcon.DOWNLOAD, SyncState.NOT_SYNCED),
            resolve(
                autoSyncEnabled = true,
                isMaster = false,
                operation = SyncOperation.NONE,
                recordedVersion = null,
                currentVersion = "7"
            )
        )
    }

    @Test
    fun when_no_history_and_check_failed_then_unknown() {
        assertEquals(
            SyncStatus(SyncIcon.UPLOAD, SyncState.UNKNOWN),
            resolve(
                autoSyncEnabled = true,
                isMaster = true,
                operation = SyncOperation.NONE,
                recordedVersion = null,
                currentVersion = null,
                driveChecked = false
            )
        )
    }

    @Test
    fun when_recorded_version_and_missing_current_then_outdated() {
        assertEquals(
            SyncStatus(SyncIcon.DOWNLOAD, SyncState.OUTDATED),
            resolve(
                autoSyncEnabled = true,
                isMaster = false,
                operation = SyncOperation.RESTORE,
                recordedVersion = "7",
                currentVersion = null
            )
        )
    }

    @Test
    fun when_no_recorded_version_and_missing_current_then_not_synced() {
        assertEquals(
            SyncStatus(SyncIcon.UPLOAD, SyncState.NOT_SYNCED),
            resolve(
                autoSyncEnabled = true,
                isMaster = true,
                operation = SyncOperation.NONE,
                recordedVersion = null,
                currentVersion = null
            )
        )
    }

    @Test
    fun when_check_failed_with_history_then_unknown() {
        assertEquals(
            SyncStatus(SyncIcon.UPLOAD, SyncState.UNKNOWN),
            resolve(
                autoSyncEnabled = false,
                operation = SyncOperation.BACKUP,
                recordedVersion = "7",
                currentVersion = "7",
                driveChecked = false
            )
        )
    }

    @Test
    fun when_automatic_disabled_and_no_operation_then_sync_and_not_synced() {
        assertEquals(
            SyncStatus(SyncIcon.SYNC, SyncState.NOT_SYNCED),
            resolve(
                autoSyncEnabled = false,
                operation = SyncOperation.NONE,
                recordedVersion = null,
                currentVersion = "7"
            )
        )
    }

    @Test
    fun when_recorded_version_is_blank_then_not_synced() {
        assertEquals(
            SyncStatus(SyncIcon.SYNC, SyncState.NOT_SYNCED),
            resolve(
                autoSyncEnabled = false,
                operation = SyncOperation.NONE,
                recordedVersion = "   ",
                currentVersion = "7"
            )
        )
    }
}
