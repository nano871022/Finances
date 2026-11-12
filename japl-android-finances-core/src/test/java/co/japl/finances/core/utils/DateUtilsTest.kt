package co.japl.finances.core.utils

import co.com.japl.ui.utils.DateUtils as UiDateUtils
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

class DateUtilsTest {

    @Test
    fun cutOffLastMonth_twoArgs_matchesUiImplementation() {
        val cases = listOf(
            15 to LocalDateTime.of(2026, 11, 15, 10, 0, 0),
            31 to LocalDateTime.of(2026, 3, 31, 10, 0, 0),
            1 to LocalDateTime.of(2026, 11, 1, 10, 0, 0),
            28 to LocalDateTime.of(2026, 2, 28, 10, 0, 0),
            10 to LocalDateTime.of(2027, 1, 10, 10, 0, 0),
            5 to LocalDateTime.of(2026, 5, 20, 10, 0, 0)
        )
        for ((day, cutOff) in cases) {
            val expected = UiDateUtils.cutOffLastMonth(day.toShort(), cutOff)
            val actual = DateUtils.cutOffLastMonth(day.toShort(), cutOff)
            assertEquals("day=$day cutOff=$cutOff", expected, actual)
        }
    }

    @Test
    fun cutOffLastMonth_withDate_matchesUiImplementation() {
        val cases = listOf(
            15 to LocalDate.of(2026, 11, 10),
            15 to LocalDate.of(2026, 11, 20),
            31 to LocalDate.of(2026, 2, 10),
            1 to LocalDate.of(2026, 12, 31),
            28 to LocalDate.of(2026, 2, 15),
            5 to LocalDate.of(2026, 5, 20)
        )
        for ((day, date) in cases) {
            val currentCutOff = UiDateUtils.cutOff(day.toShort(), date)
            val expected = UiDateUtils.cutOffLastMonth(day.toShort(), currentCutOff)
            val actual = DateUtils.cutOffLastMonth(day.toShort(), date)
            assertEquals("day=$day date=$date", expected, actual)
        }
    }

    @Test
    fun cutOffLastMonth_noArg_doesNotThrow() {
        DateUtils.cutOffLastMonth(15)
    }
}