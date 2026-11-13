package co.japl.android.myapplication.finanzas.sync

import org.junit.Assert.assertEquals
import org.junit.Test

class AutoSyncPolicyTest {

    private val localNewer = 2_000L
    private val remoteOlder = 1_000L
    private val remoteNewer = 3_000L

    @Test
    fun when_master_and_no_remote_then_backup() {
        assertEquals(
            SyncAction.BACKUP,
            AutoSyncPolicy.decide(true, localNewer, true, null)
        )
    }

    @Test
    fun when_master_and_local_is_newer_then_backup() {
        assertEquals(
            SyncAction.BACKUP,
            AutoSyncPolicy.decide(true, localNewer, true, remoteOlder)
        )
    }

    @Test
    fun when_master_and_remote_is_newer_then_none() {
        assertEquals(
            SyncAction.NONE,
            AutoSyncPolicy.decide(true, localNewer, true, remoteNewer)
        )
    }

    @Test
    fun when_master_and_timestamps_are_equal_then_none() {
        assertEquals(
            SyncAction.NONE,
            AutoSyncPolicy.decide(true, localNewer, true, localNewer)
        )
    }

    @Test
    fun when_master_and_local_has_no_data_then_none_even_without_remote() {
        assertEquals(
            SyncAction.NONE,
            AutoSyncPolicy.decide(true, localNewer, false, null)
        )
        assertEquals(
            SyncAction.NONE,
            AutoSyncPolicy.decide(true, localNewer, false, remoteOlder)
        )
    }

    @Test
    fun when_master_and_local_timestamp_is_zero_then_none() {
        assertEquals(
            SyncAction.NONE,
            AutoSyncPolicy.decide(true, 0L, true, null)
        )
    }

    @Test
    fun when_slave_and_no_remote_then_none() {
        assertEquals(
            SyncAction.NONE,
            AutoSyncPolicy.decide(false, localNewer, true, null)
        )
    }

    @Test
    fun when_slave_and_remote_is_newer_then_restore() {
        assertEquals(
            SyncAction.RESTORE,
            AutoSyncPolicy.decide(false, localNewer, true, remoteNewer)
        )
    }

    @Test
    fun when_slave_and_local_is_newer_then_none() {
        assertEquals(
            SyncAction.NONE,
            AutoSyncPolicy.decide(false, localNewer, true, remoteOlder)
        )
    }

    @Test
    fun when_slave_and_timestamps_are_equal_then_none() {
        assertEquals(
            SyncAction.NONE,
            AutoSyncPolicy.decide(false, localNewer, true, localNewer)
        )
    }

    @Test
    fun when_slave_and_local_has_no_data_then_restore() {
        assertEquals(
            SyncAction.RESTORE,
            AutoSyncPolicy.decide(false, localNewer, false, remoteNewer)
        )
        assertEquals(
            SyncAction.RESTORE,
            AutoSyncPolicy.decide(false, 0L, false, remoteNewer)
        )
    }
}