package co.com.japl.finances.iports.pojo

import java.time.LocalDateTime

data class BackupStorageInfo(
    val spaceUsed: Long,
    val spaceMax: Long,
    val lastBackup: LocalDateTime?,
    val spaceDBKb: Long
)
