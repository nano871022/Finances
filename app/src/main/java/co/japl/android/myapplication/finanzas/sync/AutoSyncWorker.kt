package co.japl.android.myapplication.finanzas.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker.Result
import androidx.work.WorkerParameters
import co.japl.android.myapplication.finanzas.modules.EntryPoint
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AutoSyncWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val entryPoint = EntryPointAccessors.fromApplication(
            applicationContext,
            EntryPoint::class.java
        )
        val prefs = entryPoint.getPrefs()

        if (!prefs.autoSyncEnabled) return@withContext Result.success()

        val account = GoogleSignIn.getLastSignedInAccount(applicationContext)
            ?: return@withContext Result.success()

        val driveGranted = account.grantedScopes
            ?.any { it.scopeUri.contains("drive") } == true
        if (!driveGranted) return@withContext Result.success()

        val driveSvc = entryPoint.getGoogleDriveSvc()

        try {
            val local = LocalDatabaseStateReader(
                applicationContext,
                entryPoint.getConnectDB()
            ).read()
            val remoteModified = driveSvc.getBackupModifiedTime(account)

            when (AutoSyncPolicy.decide(
                isMaster = prefs.autoSyncMaster,
                localModified = local.modified,
                localHasData = local.hasData,
                remoteModified = remoteModified
            )) {
                SyncAction.BACKUP -> {
                    val result = driveSvc.backup(account)
                    if (result.isNullOrBlank() || result == "0") Result.retry()
                    else Result.success()
                }

                SyncAction.RESTORE -> {
                    val result = driveSvc.restore(account)
                    if (result.isNullOrBlank()) Result.retry()
                    else Result.success()
                }

                SyncAction.NONE -> Result.success()
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.retry()
        }
    }
}