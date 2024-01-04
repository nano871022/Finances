package co.com.japl.ui.utils

import android.util.Log
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class WindowWidthSize(private val size: Dp?) {
    COMPACT(500.dp),
    MEDIUM(740.dp),
    LARGE(1080.dp),
    EXPANDED(null);

    fun isEqualTo(size: Dp): Boolean {
        return this.size?.compareTo(size)?:1 > 0
    }


    companion object {
        fun fromDp(dp: Dp): WindowWidthSize {
            Log.w("fromDp","== fromDP $dp")
            return when {
                COMPACT.isEqualTo(dp) -> COMPACT
                MEDIUM.isEqualTo(dp) -> MEDIUM
                LARGE.isEqualTo(dp) -> LARGE
                EXPANDED.isEqualTo(dp) -> EXPANDED
                else -> COMPACT
            }
        }
    }
}