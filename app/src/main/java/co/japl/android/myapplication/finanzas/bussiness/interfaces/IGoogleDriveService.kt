package co.japl.android.myapplication.finanzas.bussiness.interfaces

import com.google.android.gms.auth.api.signin.GoogleSignInAccount

interface IGoogleDriveService {

    suspend fun restore(account: GoogleSignInAccount?):String?

    suspend fun backup(account: GoogleSignInAccount?):String?

    suspend fun infoBackup(account: GoogleSignInAccount?):String

}