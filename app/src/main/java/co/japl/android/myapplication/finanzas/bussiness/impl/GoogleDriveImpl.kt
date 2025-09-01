package co.japl.android.myapplication.finanzas.bussiness.impl

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IGoogleDriveService
import co.japl.android.myapplication.utils.DatabaseConstants
import com.google.android.gms.auth.api.identity.AuthorizationResult
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.FileContent
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.util.Collections

class GoogleDriveImpl constructor(private val context:Context,private val dbConnect:ConnectDB) : IGoogleDriveService {
    companion object {
        const val PARAMETER_FOLDER = "appDataFolder"
    }

    override suspend fun stats(): List<Pair<String, Long>> {
        try {
            return getFileDB()?.let { file ->
                val db = SQLiteDatabase.openDatabase(file.absolutePath, null, SQLiteDatabase.OPEN_READONLY)
                return co.japl.android.finances.services.DB.connections.ConnectDB(context).onStats(db)
            }?: emptyList()

        }catch(e:Exception){
            return emptyList()
        }
    }

    override suspend fun restore(account: GoogleSignInAccount?): String? {
        return getDrive(account)?.let{drive->
            getFiles(drive).takeIf { it.isNotEmpty() }
                ?.firstOrNull { it.name == DatabaseConstants.DATA_BASE_NAME }?.let{
                    saveInTempFile(it.id,drive)?.let{
                        readAndRestoreDB(it)
                    }
                }
        }
    }

    private fun readAndRestoreDB(fileTempDB:java.io.File):String?{
        val db = SQLiteDatabase.openDatabase(fileTempDB.absolutePath, null, SQLiteDatabase.OPEN_READONLY)
        dbConnect.onRestore(dbConnect.writableDatabase,db)
        return "== Finished Restore Process"
    }

    private fun saveInTempFile(idFile:String,drive:Drive):java.io.File?{
        val file = java.io.File.createTempFile("${DatabaseConstants.DATA_BASE_NAME}",".db.db")
        val outputStream = FileOutputStream(file)
        drive.files().get(idFile).executeMediaAndDownloadTo(outputStream)
        outputStream.flush()
        outputStream.close()
        return file
    }

    private fun getFiles(drive:Drive):List<File>{
        return drive.files().list()
            .setSpaces(PARAMETER_FOLDER)
            .setFields("nextPageToken, files(id, name, version, modifiedTime)")
            .execute()?.files?: emptyList()
    }
    private fun getDrive(account: GoogleSignInAccount?):Drive?{
        return account?.let {

            GoogleAccountCredential.usingOAuth2(context, listOf(DriveScopes.DRIVE_FILE))?.let{
                it.selectedAccount = account.account
                return Drive.Builder(AndroidHttp.newCompatibleTransport(), GsonFactory(), it)
                    .setApplicationName("Finanzas")
                    .build()
            }
        }
    }

    fun getDrive(authorizationResult:AuthorizationResult):Drive?{
       return GoogleAccountCredential.usingOAuth2(context, listOf(DriveScopes.DRIVE_FILE))?.let{
            it.selectedAccount = authorizationResult.toGoogleSignInAccount()?.account
            return Drive.Builder(AndroidHttp.newCompatibleTransport(), GsonFactory(), it)
                .setApplicationName("Finanzas")
                .build()
        }
    }

    override suspend fun backup(account: GoogleSignInAccount?): String? {
        return getDrive(account)?.let{drive->
            getFiles(drive).takeIf { it.isNotEmpty() }
                ?.filter{ it.name == DatabaseConstants.DATA_BASE_NAME }
                ?.firstOrNull{it.size == 1}?.let{
                    update(it.id,drive)
                }?: create(drive)

        }
    }

    private fun getFileDB():java.io.File?{
        return context.applicationContext.getDatabasePath(DatabaseConstants.DATA_BASE_NAME)
    }

    private fun update(idFile:String,drive:Drive):String?{
        return getFileDB()?.let {file->
            val mimeType = Files.probeContentType(file.toPath())
            val mediaContent = FileContent(mimeType, file)
            drive.Files().update(idFile, null, mediaContent).execute()?.let{
                "== Finished Backup Process - Update Size: ${file.length() / (1024)}Kb"
            }
        }?:"== NOT-BACKUP-UPDATE"
    }

    private fun create(drive: Drive):String?{
        return getFileDB()?.let{file->
            val mimeType = Files.probeContentType(file.toPath())
            val mediaContent = FileContent(mimeType, file)
            val fileMetadata = File()
            fileMetadata.name = DatabaseConstants.DATA_BASE_NAME
            fileMetadata.mimeType = mimeType
            fileMetadata.parents = Collections.singletonList(PARAMETER_FOLDER)
            drive.Files().create(fileMetadata,mediaContent)
                .setFields("id").execute()?.let{
                    "== Finished Backup Process - Create Size: ${file.length() / (1024)}Kb"
                }
        }?:"== NOT-BACKUP-CREATE"
    }

    override suspend fun infoBackup(account: GoogleSignInAccount?): String {
        return getDrive(account)?.let{drive->
            getFiles(drive).takeIf { it.isNotEmpty() }
                ?.firstOrNull { it.name == DatabaseConstants.DATA_BASE_NAME }?.let{
                    "== ${it.name} V ${it.version}  \nLast Modified:${it.modifiedTime}"

        }}?: "== NOT-INFO"
    }


}