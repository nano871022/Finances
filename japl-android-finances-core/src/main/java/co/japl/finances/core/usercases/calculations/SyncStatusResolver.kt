package co.japl.finances.core.usercases.calculations

import co.com.japl.finances.iports.dtos.SyncIcon
import co.com.japl.finances.iports.dtos.SyncState
import co.com.japl.finances.iports.dtos.SyncStatus
import co.com.japl.finances.iports.enums.SyncOperation
import co.com.japl.finances.iports.pojo.SyncHistory

/**
 * Pure decision for the Sync tab badge. It has no Android or Compose dependencies.
 */
object SyncStatusResolver {

    fun resolve(
        autoSyncEnabled: Boolean,
        isMaster: Boolean,
        history: SyncHistory,
        currentDriveVersion: String?,
        driveChecked: Boolean
    ): SyncStatus {
        val icon = when {
            autoSyncEnabled && isMaster -> SyncIcon.UPLOAD
            autoSyncEnabled && !isMaster -> SyncIcon.DOWNLOAD
            history.operation == SyncOperation.BACKUP -> SyncIcon.UPLOAD
            history.operation == SyncOperation.RESTORE -> SyncIcon.DOWNLOAD
            else -> SyncIcon.SYNC
        }

        val state = when {
            !driveChecked -> SyncState.UNKNOWN
            history.driveVersion.isNullOrBlank() -> SyncState.NOT_SYNCED
            currentDriveVersion.isNullOrBlank() -> SyncState.OUTDATED
            history.driveVersion == currentDriveVersion -> SyncState.UPDATED
            else -> SyncState.OUTDATED
        }

        return SyncStatus(icon, state)
    }
}