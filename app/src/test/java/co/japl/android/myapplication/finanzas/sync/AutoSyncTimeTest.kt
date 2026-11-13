package co.japl.android.myapplication.finanzas.sync

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.ZoneId
import java.time.ZonedDateTime

class AutoSyncTimeTest {

    private val zone = ZoneId.of("UTC")

    @Test
    fun when_now_is_before_the_hour_then_delay_is_within_the_same_day() {
        val now = ZonedDateTime.of(2026, 11, 13, 1, 30, 0, 0, zone)

        assertEquals(30 * 60 * 1000L, AutoSyncTime.millisUntilNextHour(now, 2))
    }

    @Test
    fun when_now_is_after_the_hour_then_delay_is_until_tomorrow() {
        val now = ZonedDateTime.of(2026, 11, 13, 2, 30, 0, 0, zone)

        assertEquals((23 * 60 + 30) * 60 * 1000L, AutoSyncTime.millisUntilNextHour(now, 2))
    }

    @Test
    fun when_now_is_exactly_the_hour_then_delay_is_one_day() {
        val now = ZonedDateTime.of(2026, 11, 13, 2, 0, 0, 0, zone)

        assertEquals(24 * 60 * 60 * 1000L, AutoSyncTime.millisUntilNextHour(now, 2))
    }

    @Test
    fun when_hour_is_out_of_range_then_it_is_clamped() {
        val now = ZonedDateTime.of(2026, 11, 13, 22, 0, 0, 0, zone)

        assertEquals(60 * 60 * 1000L, AutoSyncTime.millisUntilNextHour(now, 25))
        assertEquals(2 * 60 * 60 * 1000L, AutoSyncTime.millisUntilNextHour(now, -1))
    }
}