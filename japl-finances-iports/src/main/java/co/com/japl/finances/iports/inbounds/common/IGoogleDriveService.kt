package co.com.japl.finances.iports.inbounds.common

import co.com.japl.finances.iports.dtos.StorageInfo

interface IGoogleDriveService {
    suspend fun infoBackup(account: Any): String?
    suspend fun getStorageInfo(account: Any): StorageInfo?
    suspend fun backup(account: Any): Long?
    suspend fun restore(account: Any): String?
    suspend fun stats(): List<Pair<String, Long>>?
}
