package co.japl.android.myapplication.finanzas.bussiness.impl

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import co.japl.android.myapplication.finanzas.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IGoogleDriveService
import co.japl.android.myapplication.finanzas.pojo.BackupStorageInfo
import co.japl.android.myapplication.utils.DatabaseConstants
import co.japl.android.myapplication.utils.NumbersUtil
import com.google.android.gms.auth.api.identity.AuthorizationResult
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.googleapis.json.GoogleJsonResponseException
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
        try {
            val file = java.io.File.createTempFile("${DatabaseConstants.DATA_BASE_NAME}", ".db.db")
            val outputStream = FileOutputStream(file)
            drive.files().get(idFile).executeMediaAndDownloadTo(outputStream)
            outputStream.flush()
            outputStream.close()
            return file
        } catch (e: Exception) {
            Log.e(this.javaClass.name, "Error saving temp file: ${e.message}", e)
            return null
        }
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
        val accountObj = account?.account
        val email = account?.email?.takeIf { it.isNotBlank() }
        if (accountObj == null && email == null) return null

        val transport = GoogleNetHttpTransport.newTrustedTransport()
        val credential = GoogleAccountCredential.usingOAuth2(context, SCOPES).apply {
            if (accountObj != null) {
                selectedAccount = accountObj
            } else {
                selectedAccountName = email
            }
        }
        return Drive.Builder(transport, GsonFactory.getDefaultInstance(), credential)
            .setApplicationName("Finanzas")
            .build()
    }

    fun getDrive(authorizationResult:AuthorizationResult):Drive? {
        val transport = GoogleNetHttpTransport.newTrustedTransport()
        val account = authorizationResult.toGoogleSignInAccount()
        val accountObj = account?.account
        val email = account?.email?.takeIf { it.isNotBlank() }
        if (accountObj == null && email == null) return null

        val credential = GoogleAccountCredential.usingOAuth2(context, SCOPES).apply {
            if (accountObj != null) {
                selectedAccount = accountObj
            } else {
                selectedAccountName = email
            }
        }
        return Drive.Builder(transport, GsonFactory.getDefaultInstance(), credential)
            .setApplicationName("Finanzas")
            .build()
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
        return try {
            getFileDB()?.let { file ->
                val mimeType = Files.probeContentType(file.toPath())
                val mediaContent = FileContent(mimeType, file)
                drive.files().update(idFile, null, mediaContent).execute()?.let {
                    NumbersUtil.bytesConvert(file.length())
                }
            } ?: "0 B"
        } catch (e: Exception) {
            Log.e(this.javaClass.name, "Error updating backup: ${e.message}", e)
            "0 B"
        }
    }

    private fun create(drive: Drive): String {
        return try {
            getFileDB()?.let { file ->
                val mimeType = Files.probeContentType(file.toPath())
                val mediaContent = FileContent(mimeType, file)
                val fileMetadata = File()
                fileMetadata.name = DatabaseConstants.DATA_BASE_NAME
                fileMetadata.mimeType = mimeType
                fileMetadata.parents = Collections.singletonList(PARAMETER_FOLDER)
                drive.files().create(fileMetadata, mediaContent)
                    .setFields("id").execute()?.let {
                        file.length().toString()
                    }
            } ?: "0"
        } catch (e: Exception) {
            Log.e(this.javaClass.name, "Error creating backup: ${e.message}", e)
            "0"
        }
    }

    override suspend fun infoBackup(account: GoogleSignInAccount?): String {
        return try {
            getDrive(account)?.let { drive ->
                getFiles(drive).takeIf { it.isNotEmpty() }
                    ?.firstOrNull { it.name == DatabaseConstants.DATA_BASE_NAME }?.let {
                        "== ${it.name} V ${it.version}  \nLast Modified:${it.modifiedTime}"

                    }
            } ?: "== NOT-INFO"
        } catch (e: Exception) {
            Log.e(this.javaClass.name, "Error getting info backup: ${e.message}", e)
            "== ERROR-INFO: ${e.message}"
        }
    }

    override suspend fun getStorageInfo(account: GoogleSignInAccount?): BackupStorageInfo? {
        return try {
            getDrive(account)?.let { drive ->
                val about = drive.about().get().setFields("storageQuota").execute()
                val quota = about.storageQuota
                val files = getFiles(drive)
                val backupFile = files.firstOrNull { it.name == DatabaseConstants.DATA_BASE_NAME }

                val lastBackup = backupFile?.modifiedTime?.let {
                    LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(it.value), ZoneId.systemDefault())
                }

                BackupStorageInfo(
                    spaceUsed = (quota.usage?: 0L) ,
                    spaceMax = (quota.limit?: 0L) ,
                    lastBackup = lastBackup,
                    spaceDBKb = (backupFile?.size?.toLong()?:0L)
                )
            }
        } catch (e: GoogleJsonResponseException) {
            Log.e(this.javaClass.name, "Google JSON Error: ${e.details}", e)
            null
        } catch (e: IllegalArgumentException) {
            Log.e(this.javaClass.name, "Illegal Argument Error: ${e.message}", e)
            null
        } catch (e: Exception) {
            Log.e(this.javaClass.name, "General Error: ${e.message}", e)
            null
        }
    }



}