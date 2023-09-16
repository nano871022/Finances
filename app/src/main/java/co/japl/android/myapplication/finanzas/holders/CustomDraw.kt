package co.japl.android.myapplication.finanzas.holders

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.util.AttributeSet
import android.util.Log
import android.view.View
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.pojo.PiecePie
import co.japl.android.myapplication.utils.NumbersUtil

class CustomDraw constructor (context:Context, attributeSet: AttributeSet):View(context,attributeSet) {
    val posX = 10
    val posY = 40
    val withSize = 300
    val heightSize = 300
    val reduce = 20
    var lastYTitle = 10f
    val posXTitle = posX + withSize + 5f
    val incrementYTitle = 25

    private val draws = mutableListOf<PiecePie>()

    private val drawable : ShapeDrawable = kotlin.run {
        contentDescription = "customdraw"

        ShapeDrawable(OvalShape()).apply {
            paint.color = context.resources.getColor(R.color.green_background)
            setBounds(posX,posY,posX +withSize, posY + heightSize)
        }
    }

    override fun onDraw(canvas:Canvas){
        drawable.draw(canvas)
        val total = draws.sumOf { it.value }
        var start = 0f
        drawTotal("Total: ",total, getPaint(Color.BLACK),canvas)
        draws.forEach {
            val paint = getPaint(it.color)
            val end = calculate(calculatePercent(it.value,total))
            drawPieceOfPie(start, end, paint, canvas)
            drawTitle(it.title,it.value,paint,canvas)
            start += end
        }

    }

    private fun getPaint(color:Int):Paint{
        val paint = Paint()
        paint.strokeWidth = 1f
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = color
        paint.textSize = 14f
        return paint
    }

    private fun calculatePercent(valuePiece:Double,totalValue:Double):Float{
        return ((valuePiece * 100) / totalValue).toFloat()
    }

    private fun calculate(percent:Float):Float{
        return (percent * 360) / 100
    }


    fun addPiece(title:String,value:Double,color:Int){
        val piecePie = PiecePie(color,title,value)
        draws.any { it.title == title && it.color == color }?.takeIf { !it }?.let{
            draws.add(piecePie)
        }
    }

    private fun drawTitle(title:String,value:Double,paint:Paint,canvas:Canvas){
        canvas.drawText(title,posXTitle,lastYTitle,paint)
        canvas.drawText(NumbersUtil.toString(value),posXTitle + incrementYTitle,lastYTitle+incrementYTitle,paint)
        lastYTitle += (2*incrementYTitle)
    }

    private fun drawTotal(title:String,value:Double,paint:Paint,canvas:Canvas){
        canvas.drawText(title+NumbersUtil.toString(value),10f,10f,paint)
    }

    private fun drawPieceOfPie(start:Float,end:Float,paint:Paint,canvas:Canvas){
        canvas.drawArc((posX + reduce).toFloat(),(posY + reduce).toFloat(),(posX +  (withSize - reduce)).toFloat(),(posY + (heightSize - reduce)).toFloat(),start,end,true,paint)
    }


    fun clear(){
        draws.clear()
        lastYTitle = 10f
    }
}