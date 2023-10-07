package co.japl.android.myapplication.finanzas.graphs.colors

import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.pojo.PiecePie

class Colors constructor(val context:Context,val draws:MutableList<PiecePie>){

    val colors = arrayOf(
        R.color.graphic1,
        R.color.graphic2,
        R.color.graphic3,
        R.color.graphic4,
        R.color.graphic5,
        R.color.graphic6,
        R.color.graphic7,
        R.color.graphic8,
        R.color.graphic9,
        R.color.graphic10,
        R.color.graphic11,
        R.color.graphic12,
        R.color.graphic13 ,
        R.color.graphic14,
        R.color.graphic15,
        R.color.graphic16,
        R.color.graphic17,
        R.color.graphic18,
        R.color.graphic19,
        R.color.graphic20)
    @RequiresApi(Build.VERSION_CODES.O)
    fun getColor(color:Int):Int{
        val cColor = context.getColor(color)
        return Color.valueOf(cColor).toArgb()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getColor(): Int {
        val colorValue = context.getColor(colors[draws.size])
        return Color.valueOf(colorValue).toArgb()
    }
}