package co.com.japl.finances.iports.dtos

   data class SyncStatus(
       val icon: SyncIcon,
       val state: SyncState
   )

   enum class SyncIcon {
       UPLOAD,
       DOWNLOAD,
       SYNC
   }

   enum class SyncState {
       UPDATED,
       OUTDATED,
       NOT_SYNCED,
       UNKNOWN
   }