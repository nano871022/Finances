package co.japl.android.myapplication.finanzas.bussiness.impl

import android.app.Activity
import android.content.Intent
import android.util.Log
import co.japl.android.myapplication.finanzas.bussiness.config.GoogleDriveConfig
import co.japl.android.myapplication.finanzas.bussiness.interfaces.IGoogleLoginService
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
import java.util.Collections

class GoogleDriveService(private val activity:Activity, private val config:GoogleDriveConfig,
                         override val RC_SIGN_IN: Int
):IGoogleLoginService {
    private var signInAccount: GoogleSignInAccount? = null

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
    override fun read() {
        getConnection()?.let {
            getDrive(it)?.let {
                readFiles(it)
            }
        }
    }

    override fun upload() {
        getConnection()?.let {
            getDrive(it)?.let {
                createFile(it)
            }
        }
    }

    private fun handleSignIn(data: Intent){
        val getAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
        if(getAccountTask.isSuccessful){
            getAccountTask.result?.let { getDrive(it)}
        }
    }

    private fun getDrive(account:GoogleSignInAccount):Drive?{
        val credential = GoogleAccountCredential.usingOAuth2(activity, listOf(DriveScopes.DRIVE,DriveScopes.DRIVE_FILE,DriveScopes.DRIVE_APPDATA,
            DriveScopes.DRIVE_METADATA, "https://www.googleapis.com/auth/drive.appfolder"))
        credential.selectedAccount = account.account
        return Drive.Builder(AndroidHttp.newCompatibleTransport(), GsonFactory(), credential)
            .setApplicationName("Finanzas")
            .build()
            .also { Log.d(javaClass.name,"=== HandleSignIn2 $it") }
    }
    private fun createFolder(drive:Drive) {

            CoroutineScope(Dispatchers.IO).launch {
                try{
                val folder = File()
                folder.name = "Finanzas"
                folder.mimeType = "application/vnd.google-apps.folder"
                drive.Files().create(folder).setFields("id").execute()
                } catch (e: Exception) {
                    Log.e(javaClass.name, "ERROR ${e.message}", e)
                }
            }

    }



    private fun createFile(drive: Drive){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val fileMetadata = File()
                fileMetadata.name = "test.txt"
                fileMetadata.mimeType = "text/plain"
                fileMetadata.parents = Collections.singletonList("appDataFolder")
                val filePath = java.io.File("files/test.txt")
                val mediaContent = FileContent("text/plain", filePath)
                val file = drive.Files().create(fileMetadata,mediaContent)
                    .setFields("id")
                    .execute()
                Log.d(javaClass.name,"=== createFile ${file.id}")
            }catch(e:Exception){
                Log.e(javaClass.name, "ERROR ${e.message}", e)
            }
        }
    }

    private fun readFiles(drive: Drive) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                drive.Files().list()
                    .setSpaces("appDataFolder")
                    .setFields("nextPageToken, files(id, name)")
                    .setPageSize(10)
                    .execute()
                    ?.files
                    ?.forEach {
                        Log.d(javaClass.name, "=== ${it.name} ${it.id}")
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







