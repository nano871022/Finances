package co.japl.android.myapplication.finanzas.holders

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toColor
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.pojo.PiecePie
import co.japl.android.myapplication.utils.NumbersUtil
import java.util.Random

class CustomDraw constructor (context:Context, attributeSet: AttributeSet):View(context,attributeSet){
    val minSize = if(resources.displayMetrics.widthPixels < resources.displayMetrics.heightPixels) {
        resources.displayMetrics.widthPixels
    }else{
        resources.displayMetrics.heightPixels
    }
    val colors = arrayOf(R.color.graphic1,R.color.graphic2,R.color.graphic3,R.color.graphic4,R.color.graphic5,R.color.graphic6,R.color.graphic7,R.color.graphic8,R.color.graphic9,R.color.graphic10,R.color.graphic11,R.color.graphic12,R.color.graphic13 ,R.color.graphic14,R.color.graphic15,R.color.graphic16,R.color.graphic17,R.color.graphic18,R.color.graphic19,R.color.graphic20)
    @RequiresApi(Build.VERSION_CODES.R)
    val nightMode = resources.configuration.isNightModeActive
    var lastTouchY = 0f
    var lastTouchX = 0f
    val fontSize = 14
    val posX = (resources.displayMetrics.density * fontSize).toInt()
    val posY = (resources.displayMetrics.density * 40).toInt()
    var withSize = (resources.displayMetrics.density * minSize / 2).toInt()
    var heightSize = (resources.displayMetrics.density * minSize / 2).toInt()
    val reduce = (resources.displayMetrics.density * 20)
    var lastYTitle = ((resources.displayMetrics.density * fontSize )+ 20)
    val posXTitle = posX + withSize + 5f
    var incrementYTitle = (resources.displayMetrics.density * fontSize) + 5

    private val draws = mutableListOf<PiecePie>()

        override fun onTouchEvent(event: MotionEvent?): Boolean {
            val x = event?.x
            val y = event?.y
            when(event?.action){
                MotionEvent.ACTION_DOWN -> {
                    if (x != null && y != null){
                        lastTouchX = x
                        lastTouchY = y
                    }
                    invalidate()
                }
            }
            return true
    }

    private val drawable : ShapeDrawable = kotlin.run {
        contentDescription = "customdraw"

        ShapeDrawable(OvalShape()).apply {
            Log.d(javaClass.name,"withSize: $withSize, heightSize: $heightSize")
            paint.color = context.resources.getColor(R.color.green_background)
            setBounds(posX,posY,posX +withSize, posY + heightSize)
        }
    }

       @RequiresApi(Build.VERSION_CODES.R)
       override fun onDraw(canvas:Canvas){
        drawable.draw(canvas)
           val bitMap = drawable.toBitmap(500,500)
           lastYTitle = ((resources.displayMetrics.density * fontSize )+ 20)
        val total = draws.sumOf { it.value }
        var start = 0f
        val colorTotal = if(nightMode){Color.WHITE}else{ Color.BLACK}
        drawTotal("Total: ",total, getPaint(colorTotal),canvas)
        draws.forEach {
            val paint = getPaint(it.color)
            val end = calculate(calculatePercent(it.value,total))
            drawPieceOfPie(start, end, paint, canvas)
            drawTitle(it.title,it.value,paint,canvas,colorTotal)
            start += end
        }

           if(lastTouchX > 0 && lastTouchX < 200 && lastTouchY > 0 && lastTouchY < 200){
               val paint = getPaint(Color.RED)
               val pix = bitMap.getColor(lastTouchX.toInt(),lastTouchY.toInt())
               canvas.drawText("Touch $pix",lastTouchX,lastTouchY,paint)

           }

    }

    private fun getPaint(color:Int):Paint{
        val density = resources.displayMetrics.density
        val paint = Paint()
        paint.strokeWidth = 1f
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = color
        paint.textSize = density * fontSize
        return paint
    }

    private fun calculatePercent(valuePiece:Double,totalValue:Double):Float{
        return ((valuePiece * 100) / totalValue).toFloat()
    }

    private fun calculate(percent:Float):Float{
        return (percent * 360) / 100
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getColor(): Int {
        val colorValue = context.getColor(colors[draws.size])
        return Color.valueOf(colorValue).toArgb()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addPiece(title:String, value:Double){
        val color = getColor()
        val piecePie = PiecePie(color,title,value)
        draws.add(piecePie)
    }

    private fun drawTitle(title:String,value:Double,paint:Paint,canvas:Canvas,color:Int){
        canvas.drawRect(posXTitle ,lastYTitle+incrementYTitle,posXTitle + 10,lastYTitle + 10,paint)
        canvas.drawText("$title $ ${NumbersUtil.toString(value)}",posXTitle+15,lastYTitle+incrementYTitle,getPaint(color))
        //canvas.drawText(NumbersUtil.toString(value),posXTitle + incrementYTitle,lastYTitle+(2*incrementYTitle),paint)
        lastYTitle += (1*incrementYTitle)
    }

    private fun drawTotal(title:String,value:Double,paint:Paint,canvas:Canvas){
        canvas.drawText("$title $ ${NumbersUtil.toString(value)}",(resources.displayMetrics.density * fontSize),(resources.displayMetrics.density * fontSize).toFloat(),paint)
    }

    private fun drawPieceOfPie(start:Float,end:Float,paint:Paint,canvas:Canvas){
        canvas.drawArc((posX + reduce).toFloat(),(posY + reduce).toFloat(),(posX +  (withSize - reduce)).toFloat(),(posY + (heightSize - reduce)).toFloat(),start,end,true,paint)
    }


    fun clear(){
        draws.clear()
        lastYTitle = 10f
    }
}