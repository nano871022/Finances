package co.japl.finances.core.adapters.inbound.implement.common

import co.com.japl.finances.iports.inbounds.common.IGoogleDriveService
import co.com.japl.finances.iports.outbounds.IGoogleDrivePort
import co.com.japl.finances.iports.pojo.BackupStorageInfo
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import javax.inject.Inject

class GoogleDriveServiceImpl @Inject constructor(
    private val googleDrivePort: IGoogleDrivePort,
) : IGoogleDriveService {

    override suspend fun stats(): List<Pair<String, Long>> {
        return googleDrivePort.stats()
    }

    override suspend fun restore(account: GoogleSignInAccount?): String? {
        return googleDrivePort.restore(account)
    }

    override suspend fun backup(account: GoogleSignInAccount?): String? {
        return googleDrivePort.backup(account)
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
}
