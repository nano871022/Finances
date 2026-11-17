package co.com.japl.finances.iports.inbounds.common

import co.com.japl.finances.iports.pojo.BackupStorageInfo
import co.com.japl.finances.iports.dtos.SyncStatus
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface IGoogleDriveService {
    suspend fun stats():List<Pair<String,Long>>

    suspend fun restore(account: GoogleSignInAccount?):String?

    suspend fun backup(account: GoogleSignInAccount?):String?

    suspend fun infoBackup(account: GoogleSignInAccount?):String

    suspend fun getStorageInfo(account: GoogleSignInAccount?): BackupStorageInfo?

    suspend fun getBackupModifiedTime(account: GoogleSignInAccount?): Long?

    /**
     * Computes the badge shown in the Sync tab.
     *
     * [autoSyncEnabled] and [isMaster] come from the user's auto-sync settings.
     * The decision logic lives in the core module.
     */
    suspend fun getSyncStatus(
        account: GoogleSignInAccount?,
        autoSyncEnabled: Boolean,
        isMaster: Boolean
    ): SyncStatus
}
