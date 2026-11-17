package co.japl.android.myapplication.finanzas.sync

import co.com.japl.finances.iports.enums.SyncOperation
import co.com.japl.finances.iports.outbounds.ISyncHistoryPort
import co.com.japl.finances.iports.pojo.SyncHistory
import co.com.japl.ui.Prefs
import javax.inject.Inject

class SyncHistoryPort @Inject constructor(
    private val prefs: Prefs
) : ISyncHistoryPort {

    override fun read(): SyncHistory {
        val operation = SyncOperation.entries.firstOrNull {
            it.name == prefs.lastSyncOperation
        } ?: SyncOperation.NONE
        val version = prefs.lastSyncDriveVersion.takeIf { it.isNotBlank() }
        return SyncHistory(operation, version)
    }

    override fun save(history: SyncHistory) {
        prefs.saveLastSync(history.operation.name, history.driveVersion.orEmpty())
    }
}
