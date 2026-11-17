package co.com.japl.finances.iports.outbounds

import co.com.japl.finances.iports.pojo.SyncHistory

interface ISyncHistoryPort {
    fun read(): SyncHistory
    fun save(history: SyncHistory)
}
