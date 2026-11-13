package co.japl.android.myapplication.finanzas.sync

import android.content.Context
import co.japl.android.finances.services.DB.connections.ConnectDB
import co.japl.android.finances.services.utils.DatabaseConstants

data class LocalDatabaseState(
    val modified: Long,
    val hasData: Boolean
)

class LocalDatabaseStateReader(
    private val context: Context,
    private val connectDB: ConnectDB
) {

    fun read(): LocalDatabaseState {
        val dbFile = context.getDatabasePath(DatabaseConstants.DATA_BASE_NAME)
        if (!dbFile.exists()) {
            return LocalDatabaseState(modified = 0L, hasData = false)
        }

        val modified = LocalDatabaseFile.modifiedTime(dbFile)
        val hasData = connectDB.onStats(connectDB.readableDatabase)
            .any { (_, rows) -> rows > 0L }

        return LocalDatabaseState(modified = modified, hasData = hasData)
    }
}