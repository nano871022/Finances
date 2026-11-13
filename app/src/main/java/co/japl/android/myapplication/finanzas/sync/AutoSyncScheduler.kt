package co.japl.android.myapplication.finanzas.sync

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

object AutoSyncScheduler {

    const val WORK_NAME = "finances_auto_sync"

    fun schedule(context: Context, hour: Int, replace: Boolean) {
        val request = PeriodicWorkRequestBuilder<AutoSyncWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(
                AutoSyncTime.millisUntilNextHour(ZonedDateTime.now(), hour),
                TimeUnit.MILLISECONDS
            )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .addTag(WORK_NAME)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            if (replace) ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE
            else ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    fun cancel(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
    }
}