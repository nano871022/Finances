package co.com.japl.finances.iports.pojo

   import co.com.japl.finances.iports.enums.SyncOperation

   data class SyncHistory(
       val operation: SyncOperation,
       val driveVersion: String?
   )