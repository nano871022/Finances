package co.japl.android.graphs.pojo

import android.graphics.Color
import androidx.annotation.ColorInt

data class PiecePie(
    val color:Int,
    val title:String,
    val value:Double,
    val start:Float?= null,
    val end:Float?= null
)
