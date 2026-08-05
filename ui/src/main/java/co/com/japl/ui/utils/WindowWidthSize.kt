package co.com.japl.ui.utils

import android.util.Log
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class WindowWidthSize(private val size: Dp?) {
    NANO(200.dp),
    MICRO(250.dp),
    COMPACT(500.dp),
    MEDIUM(740.dp),
    MEDIUMX(800.dp),
    LARGES(980.dp),
    LARGE(1080.dp),
    EXPANDED(null);

    fun isEqualTo(size: Dp): Boolean {
        return this.size == null || this.size?.compareTo(size)!! >= 0
    }


    companion object {
        fun fromDp(dp: Dp): WindowWidthSize {
            return when {
                COMPACT.isEqualTo(dp) -> COMPACT
                MEDIUM.isEqualTo(dp) -> MEDIUM
                MEDIUMX.isEqualTo(dp) -> MEDIUMX
                LARGES.isEqualTo(dp) -> LARGES
                LARGE.isEqualTo(dp) -> LARGE
                EXPANDED.isEqualTo(dp) -> EXPANDED
                else -> COMPACT
            }
        }
    }
}