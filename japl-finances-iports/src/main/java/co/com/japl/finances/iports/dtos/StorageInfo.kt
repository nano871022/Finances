package co.com.japl.finances.iports.dtos

import java.time.LocalDateTime

data class StorageInfo(
    val spaceUsed: Long,
    val spaceMax: Long,
    val lastBackup: LocalDateTime?,
    val spaceDBKb: Long
)
