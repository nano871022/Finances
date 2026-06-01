package co.japl.android.finances.services.implement

import co.com.japl.finances.iports.inbounds.common.IGoogleDriveService
import co.com.japl.finances.iports.dtos.StorageInfo
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import java.time.LocalDateTime

class GoogleDriveServiceImpl : IGoogleDriveService {
    override suspend fun infoBackup(account: Any): String? = "No backup info available"
    override suspend fun getStorageInfo(account: Any): StorageInfo? = StorageInfo(0, 15 * 1024 * 1024, LocalDateTime.now(), 0)
    override suspend fun backup(account: Any): Long? = 0L
    override suspend fun restore(account: Any): String? = "Restore not implemented"
    override suspend fun stats(): List<Pair<String, Long>>? = emptyList()
}
