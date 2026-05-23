package co.japl.android.myapplication.finanzas.bussiness.impl

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.myapplication.finanzas.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IGoogleDriveService
import co.japl.android.myapplication.finanzas.pojo.BackupStorageInfo
import co.japl.android.myapplication.utils.DatabaseConstants
import com.google.android.gms.auth.api.identity.AuthorizationResult
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.FileContent
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.util.Collections
import java.time.LocalDateTime
import java.time.ZoneId

class GoogleDriveImpl(private val context:Context, private val dbConnect:ConnectDB) : IGoogleDriveService {
    companion object {
        const val PARAMETER_FOLDER = "appDataFolder"
        private val SCOPES = listOf(DriveScopes.DRIVE_FILE, DriveScopes.DRIVE_APPDATA)
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

    private fun readAndRestoreDB(fileTempDB:java.io.File): String {
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
        try {
            return drive.files().list()
                .setSpaces(PARAMETER_FOLDER)
                .setFields("nextPageToken, files(id, name, version, modifiedTime, size)")
                .execute()?.files ?: emptyList()
        }catch (e:Exception){
            Log.e(this.javaClass.name,e.message,e)
            return emptyList()
        }
    }
    private fun getDrive(account: GoogleSignInAccount?):Drive? {
        return account?.let {
            val transport = GoogleNetHttpTransport.newTrustedTransport()
            GoogleAccountCredential.usingOAuth2(context, SCOPES).apply {
                selectedAccountName = account.email
            }?.let { credential ->
                return Drive.Builder(transport, GsonFactory.getDefaultInstance(), credential)
                    .setApplicationName("Finanzas")
                    .build()
            }
        }
    }

    fun getDrive(authorizationResult:AuthorizationResult):Drive? {
        val transport = GoogleNetHttpTransport.newTrustedTransport()
        val account = authorizationResult.toGoogleSignInAccount()
        return GoogleAccountCredential.usingOAuth2(context, SCOPES).apply {
            selectedAccountName = account?.email
        }?.let { credential ->
            return Drive.Builder(transport, GsonFactory.getDefaultInstance(), credential)
                .setApplicationName("Finanzas")
                .build()
        }
    }

    override suspend fun backup(account: GoogleSignInAccount?): String? {
        return getDrive(account)?.let{drive->
            val files = getFiles(drive).filter { it.name == DatabaseConstants.DATA_BASE_NAME }
            if (files.isNotEmpty()) {
                update(files[0].id, drive)
            } else {
                create(drive)
            }
        }
    }

    private fun getFileDB():java.io.File?{
        return context.applicationContext.getDatabasePath(DatabaseConstants.DATA_BASE_NAME)
    }

    private fun update(idFile:String,drive:Drive): String {
        return getFileDB()?.let {file->
            val mimeType = Files.probeContentType(file.toPath())
            val mediaContent = FileContent(mimeType, file)
            drive.files().update(idFile, null, mediaContent).execute()?.let{
                "== Finished Backup Process - Update Size: ${file.length() / (1024)}Kb"
            }
        }?:"== NOT-BACKUP-UPDATE"
    }

    private fun create(drive: Drive): String {
        return getFileDB()?.let{file->
            val mimeType = Files.probeContentType(file.toPath())
            val mediaContent = FileContent(mimeType, file)
            val fileMetadata = File()
            fileMetadata.name = DatabaseConstants.DATA_BASE_NAME
            fileMetadata.mimeType = mimeType
            fileMetadata.parents = Collections.singletonList(PARAMETER_FOLDER)
            drive.files().create(fileMetadata,mediaContent)
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

    override suspend fun getStorageInfo(account: GoogleSignInAccount?): BackupStorageInfo? {
        return getDrive(account)?.let { drive ->
            val about = drive.about().get().setFields("storageQuota").execute()
            val quota = about.storageQuota
            val files = getFiles(drive)
            val backupFile = files.firstOrNull { it.name == DatabaseConstants.DATA_BASE_NAME }
            
            val lastBackup = backupFile?.modifiedTime?.let {
                LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(it.value), ZoneId.systemDefault())
            }
            val spaceDBKb = ((backupFile?.size ?: 0) /1024).toLong()
            BackupStorageInfo(
                spaceUsed = quota.usage ?: 0L,
                spaceMax = quota.limit ?: 0L,
                lastBackup = lastBackup,
                spaceDBKb = spaceDBKb
            )
        }
    }


}