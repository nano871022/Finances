package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.pojo.BackupStorageInfo
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface IGoogleDrivePort {
    suspend fun stats(): List<Pair<String, Long>>

    suspend fun restore(account: GoogleSignInAccount?): String?

    suspend fun backup(account: GoogleSignInAccount?): String?

    suspend fun infoBackup(account: GoogleSignInAccount?): String

    suspend fun getStorageInfo(account: GoogleSignInAccount?): BackupStorageInfo?

    suspend fun getBackupModifiedTime(account: GoogleSignInAccount?): Long?

   /**
    * Returns the Drive revision (`version`) of the `finances.db` backup.
    *
    * Returns `null` when there is no account/drive or no backup file.
    * Drive/network exceptions are intentionally not caught here; the core
    * adapter decides whether to surface them as an unknown status.
    */
   suspend fun getBackupVersion(account: GoogleSignInAccount?): String?
}
