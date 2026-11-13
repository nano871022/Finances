package co.japl.android.myapplication.finanzas.sync

import java.time.Duration
import java.time.ZonedDateTime

object AutoSyncTime {

    fun millisUntilNextHour(now: ZonedDateTime, hour: Int): Long {
        val targetHour = hour.coerceIn(0, 23)
        var target = now
            .withHour(targetHour)
            .withMinute(0)
            .withSecond(0)
            .withNano(0)

        if (!target.isAfter(now)) {
            target = target.plusDays(1)
        }

        return Duration.between(now, target).toMillis()
    }
}