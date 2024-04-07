package co.japl.android.myapplication.finanzas.bussiness.impl

import android.app.Activity
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import co.japl.android.finances.services.DB.connections.PaidConnectDB
import co.japl.android.finances.services.dto.PaidDB
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.finanzas.bussiness.config.GoogleDriveConfig
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IGoogleLoginService
import co.japl.android.myapplication.utils.DatabaseConstants
import com.google.android.gms.auth.UserRecoverableAuthException
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.*
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.FileContent
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.FileOutputStream
import java.io.FileReader
import java.io.InputStream
import java.util.Collections

class GoogleDriveService(private var dbConnect: SQLiteOpenHelper,private val activity:Activity, private val config:GoogleDriveConfig,
                         override val RC_SIGN_IN: Int
):IGoogleLoginService {
    private var signInAccount: GoogleSignInAccount? = null
    private var message:String? = null
    private val subscriberMessage = arrayListOf<()->Unit>()

    override fun subscribeMessage(subscriber:()->Unit) = subscriberMessage.add(subscriber)

    private fun message(message:String?) {
        message?.let {
            this.message = message
            subscriberMessage.forEach { it.invoke() }
        }
    }
    override fun message():String = message?:""

    private val googleSignInOptions by lazy{
         GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
          //  .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestScopes(Scope(DriveScopes.DRIVE_FILE))
            .build()
    }
    private val googleSignInClient = GoogleSignIn.getClient(activity,googleSignInOptions)

    override fun getIntent(): Intent = googleSignInClient.signInIntent

    override fun check(): Boolean = getConnection()?.let {
        signInAccount = it
        it.takeIf { it.grantedScopes.isNotEmpty() }?.let{
            it.grantedScopes.forEach{Log.d(this.javaClass.name,"=== GrantedScopes ${it}")}
            signInAccount?.let { getDrive(it)}
            true
        }
    }?:false

    private fun getConnection():GoogleSignInAccount?=GoogleSignIn.getLastSignedInAccount(activity)



   override fun logout(){
        googleSignInClient.signOut()
        signInAccount = null
    }

    override fun getAccount(): GoogleSignInAccount = signInAccount?:throw Exception("Not logged in")
    override fun restore() {
        getConnection()?.let {
            getDrive(it)?.let {
                readFiles(it)
            }
        }
    }

    override fun backup() {
        getConnection()?.let {
            getDrive(it)?.let {
                createFile(it)
            }
        }
    }

    override fun infoBackup() :String{
        try {
            getConnection()?.let {
                getDrive(it)?.let {
                    it.Files().list()
                        .setSpaces("appDataFolder")
                        .setFields("nextPageToken, files(id, name, version, modifiedTime)")
                        .setPageSize(10)
                        .execute()?.files?.takeIf { it.isNotEmpty() }?.filter {
                            it.name == DatabaseConstants.DATA_BASE_NAME
                        }?.first()?.let {
                            return "${it.name} V ${it.version}  \nLast Modified:${it.modifiedTime}"
                        }
                }
            }
        }catch(e:Exception){
            Log.e(javaClass.name,"ERROR ${e.message}",e)
            return "ERROR ${e.message}"
        }
        return "NOT-INFO"
    }

    private fun handleSignIn(data: Intent){
        val getAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
        if(getAccountTask.isSuccessful){
            getAccountTask.result?.let { getDrive(it)}
        }else{
            message(getAccountTask.exception?.message)
            message(getAccountTask.result?.toString())
        }
    }

    private fun getDrive(account:GoogleSignInAccount):Drive?{
        val credential = GoogleAccountCredential.usingOAuth2(activity, listOf(DriveScopes.DRIVE_FILE))
        credential.selectedAccount = account.account
        return Drive.Builder(AndroidHttp.newCompatibleTransport(), GsonFactory(), credential)
            .setApplicationName("Finanzas")
            .build()
            .also { Log.d(javaClass.name,"=== HandleSignIn2 $it") }
    }

    private fun createFile(drive: Drive){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val fileMetadata = File()
                fileMetadata.name = "${DatabaseConstants.DATA_BASE_NAME}"
                fileMetadata.mimeType = "application/db"
                fileMetadata.parents = Collections.singletonList("appDataFolder")
                val filePath = getFile()

                val mediaContent = FileContent("application/db", filePath)

                Log.d(javaClass.name,"=== uploadFiles ${filePath?.absolutePath} ${filePath?.exists()} ${(filePath?.totalSpace?:0)/(1024*1024)}Mb ${(filePath?.length()?:0)/(1024)}Kb")

                drive.Files().list().setSpaces("appDataFolder")
                    .setFields("nextPageToken, files(id, name, version)")
                    .setPageSize(10)
                    .execute().files?.takeIf { it.isNotEmpty() }?.filter {
                        it.name == DatabaseConstants.DATA_BASE_NAME
                    }?.takeIf { it.size == 1 }?.first()?.let {
                        drive.Files().update(it.id,null,mediaContent)
                            .execute()?.let {Log.d(javaClass.name, "=== updateFile ${it.id} ${it.name} ${it.size} ${it.version} ${it.driveId}")
                                message( "${message?:""} Finished Backup Process")}
                    }?:drive.let{
                    drive.Files().create(fileMetadata,mediaContent)
                        .setFields("id")
                        .execute()?.let {
                            Log.d(
                                javaClass.name,
                                "=== createFile ${it.id} ${it.name} ${it.size} ${it.version}}"
                            )
                            message( "${message?:""} Finished Backup Process")
                        }
                }
            }catch(e:Exception){
                Log.e(javaClass.name, "ERROR ${e.message}", e)
            }
        }
    }


    private fun getFile():java.io.File?{
       return activity.applicationContext.getDatabasePath(DatabaseConstants.DATA_BASE_NAME)
    }

    private fun readFiles(drive: Drive) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val file = java.io.File.createTempFile("${DatabaseConstants.DATA_BASE_NAME}",".db.db")
                val outputStream = FileOutputStream(file)
                drive.Files().list()
                    .setSpaces("appDataFolder")
                    .setFields("nextPageToken, files(id, name)")
                    .setPageSize(10)
                    .execute()?.files?.takeIf { it.isNotEmpty() }?.filter {
                        it.name == DatabaseConstants.DATA_BASE_NAME
                    }?.first()?.let {
                        drive.Files().get(it.id).executeMediaAndDownloadTo(outputStream)
                        outputStream.flush()
                        outputStream.close()
                        Log.d(javaClass.name,"=== readFiles ${file.absolutePath} ${file.exists()} ${file.totalSpace/(1024*1024)}Mb ${file.length()/(1024)}Kb ${it.id} ${it.name} ${it.size} ${it.version} ${it.driveId}")
                        val db = SQLiteDatabase.openDatabase(file.absolutePath, null, SQLiteDatabase.OPEN_READONLY)

                        (dbConnect as ConnectDB).onRestore(dbConnect.writableDatabase,db).also {
                          message( "${message?:""} Finished Restore Process")
                        }
                    }
            } catch (e: Exception) {
                Log.e(javaClass.name, "ERROR ${e.message}", e)
            }
        }
    }

    override fun response(requestCode: Int, resultCode: Int, data: Intent){
        Log.d(this.javaClass.name, "onActivityResult $requestCode $data")
        when(requestCode) {
            RC_SIGN_IN -> {
                Log.d(this.javaClass.name, "onActivityResults REQUEST CODE SIGN IN")
                if (data != null) {
                    handleSignIn(data)
                } else {
                }
            }
        }
    }



}







