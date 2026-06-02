package co.com.japl.finances.iports.inbounds.common

import co.com.japl.finances.iports.pojo.BackupStorageInfo
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface IGoogleDriveService {
    suspend fun stats():List<Pair<String,Long>>

    suspend fun restore(account: GoogleSignInAccount?):String?

    suspend fun backup(account: GoogleSignInAccount?):String?

    suspend fun infoBackup(account: GoogleSignInAccount?):String

    suspend fun getStorageInfo(account: GoogleSignInAccount?): BackupStorageInfo?
}
