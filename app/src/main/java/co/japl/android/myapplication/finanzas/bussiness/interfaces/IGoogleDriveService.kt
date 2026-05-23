package co.japl.android.myapplication.finanzas.bussiness.interfaces

import co.japl.android.myapplication.finanzas.pojo.BackupStorageInfo
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import java.time.LocalDateTime
interface IGoogleDriveService {

    suspend fun stats():List<Pair<String,Long>>

    suspend fun restore(account: GoogleSignInAccount?):String?

    suspend fun backup(account: GoogleSignInAccount?):String?

    suspend fun infoBackup(account: GoogleSignInAccount?):String

    suspend fun getStorageInfo(account: GoogleSignInAccount?): BackupStorageInfo?

}