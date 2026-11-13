package co.japl.android.myapplication.finanzas.sync

enum class SyncAction {
    BACKUP,
    RESTORE,
    NONE
}

object AutoSyncPolicy {

    fun decide(
        isMaster: Boolean,
        localModified: Long,
        localHasData: Boolean,
        remoteModified: Long?
    ): SyncAction {
        val hasLocalData = localHasData && localModified > 0L
        val remote = remoteModified ?: 0L
        val hasRemoteBackup = remote > 0L

        return if (isMaster) {
            when {
                !hasLocalData -> SyncAction.NONE
                !hasRemoteBackup -> SyncAction.BACKUP
                localModified > remote -> SyncAction.BACKUP
                else -> SyncAction.NONE
            }
        } else {
            when {
                !hasRemoteBackup -> SyncAction.NONE
                !hasLocalData -> SyncAction.RESTORE
                remote > localModified -> SyncAction.RESTORE
                else -> SyncAction.NONE
            }
        }
    }
}