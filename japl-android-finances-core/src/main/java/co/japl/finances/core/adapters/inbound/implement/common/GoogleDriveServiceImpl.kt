package co.japl.finances.core.adapters.inbound.implement.common

import co.com.japl.finances.iports.dtos.SyncStatus
import co.com.japl.finances.iports.enums.SyncOperation
import co.com.japl.finances.iports.inbounds.common.IGoogleDriveService
import co.com.japl.finances.iports.outbounds.IGoogleDrivePort
import co.com.japl.finances.iports.outbounds.ISyncHistoryPort
import co.com.japl.finances.iports.pojo.BackupStorageInfo
import co.com.japl.finances.iports.pojo.SyncHistory
import co.japl.finances.core.usercases.calculations.SyncStatusResolver
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class GoogleDriveServiceImpl @Inject constructor(
    private val googleDrivePort: IGoogleDrivePort,
    private val syncHistoryPort: ISyncHistoryPort,
) : IGoogleDriveService {

    override suspend fun stats(): List<Pair<String, Long>> {
        return googleDrivePort.stats()
    }

    override suspend fun restore(account: GoogleSignInAccount?): String? {
        val result = googleDrivePort.restore(account)
        recordSync(result, SyncOperation.RESTORE, account)
        return result
    }

    override suspend fun backup(account: GoogleSignInAccount?): String? {
        val result = googleDrivePort.backup(account)
        recordSync(result, SyncOperation.BACKUP, account)
        return result
    }

    override suspend fun infoBackup(account: GoogleSignInAccount?): String {
        return googleDrivePort.infoBackup(account)
    }

    override suspend fun getStorageInfo(account: GoogleSignInAccount?): BackupStorageInfo? {
        return googleDrivePort.getStorageInfo(account)
    }

    override suspend fun getBackupModifiedTime(account: GoogleSignInAccount?): Long? {
        return googleDrivePort.getBackupModifiedTime(account)
    }

    override suspend fun getSyncStatus(
        account: GoogleSignInAccount?,
        autoSyncEnabled: Boolean,
        isMaster: Boolean
    ): SyncStatus {
        val history = syncHistoryPort.read()
        return try {
            val currentVersion = googleDrivePort.getBackupVersion(account)
            SyncStatusResolver.resolve(
                autoSyncEnabled = autoSyncEnabled,
                isMaster = isMaster,
                history = history,
                currentDriveVersion = currentVersion,
                driveChecked = true
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            SyncStatusResolver.resolve(
                autoSyncEnabled = autoSyncEnabled,
                isMaster = isMaster,
                history = history,
                currentDriveVersion = null,
                driveChecked = false
            )
        }
    }

    private suspend fun recordSync(
        result: String?,
        operation: SyncOperation,
        account: GoogleSignInAccount?
    ) {
        if (result.isNullOrBlank() || result == "0") return

        val version = try {
            googleDrivePort.getBackupVersion(account)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            null
        }

        if (!version.isNullOrBlank()) {
            syncHistoryPort.save(SyncHistory(operation, version))
        }
    }
}
