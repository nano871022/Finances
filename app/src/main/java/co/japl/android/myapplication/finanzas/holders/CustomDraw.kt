package co.japl.android.myapplication.finanzas.holders

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ShapeDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import co.japl.android.myapplication.finanzas.graphs.interfaces.IGraph
import co.japl.android.myapplication.finanzas.graphs.pieceofpie.PieceOfPie

class CustomDraw constructor (context:Context, attributeSet: AttributeSet):View(context,attributeSet){
    val piecePie :IGraph = PieceOfPie(context)

         override fun onTouchEvent(event: MotionEvent?): Boolean {
            val x = event?.x
            val y = event?.y
            when(event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (x != null && y != null) {
                        piecePie.validateTouch(x,y)
                        invalidate()
                    }
                }
            }
            return true
    }

    private val drawable : ShapeDrawable = kotlin.run {
        contentDescription = "customdraw"
        piecePie.drawBackground()
    }

       @RequiresApi(Build.VERSION_CODES.R)
       override fun onDraw(canvas:Canvas){
           drawable.draw(canvas)
           piecePie.drawing(canvas)
       }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addPiece(title:String, value:Double){
        piecePie.addPiece(title,value)
    }

    fun clear(){
        piecePie.clear()
    }
}