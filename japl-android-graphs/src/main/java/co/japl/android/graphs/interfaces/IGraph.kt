package co.japl.android.graphs.interfaces

import android.graphics.Canvas
import android.graphics.drawable.ShapeDrawable

interface IGraph {
    fun clear()
    fun addPiece(title:String, value:Double)
    fun drawing(canvas: Canvas)
    fun validateTouch(x:Float,y:Float)
    fun drawBackground(): ShapeDrawable
}