package co.japl.android.myapplication.finanzas.holders

import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import android.service.autofill.FieldClassification.Match
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toColor
import co.japl.android.myapplication.R
import co.japl.android.myapplication.finanzas.holders.validations.isNull
import co.japl.android.myapplication.finanzas.pojo.PiecePie
import co.japl.android.myapplication.utils.NumbersUtil
import java.math.MathContext
import java.text.DecimalFormat
import java.util.Random
import kotlin.math.atan2

class CustomDraw constructor (context:Context, attributeSet: AttributeSet):View(context,attributeSet){
    val colors = arrayOf(R.color.graphic1,R.color.graphic2,R.color.graphic3,R.color.graphic4,R.color.graphic5,R.color.graphic6,R.color.graphic7,R.color.graphic8,R.color.graphic9,R.color.graphic10,R.color.graphic11,R.color.graphic12,R.color.graphic13 ,R.color.graphic14,R.color.graphic15,R.color.graphic16,R.color.graphic17,R.color.graphic18,R.color.graphic19,R.color.graphic20)
    val defaultMinSize = 300F
    var withSize = defaultMinSize.toInt()
    var heightSize = defaultMinSize.toInt()
    val squad = 10 * resources.displayMetrics.density
    val withReal = resources.displayMetrics.widthPixels / resources.displayMetrics.density
    init {
        val minSize =
            if (resources.displayMetrics.widthPixels < resources.displayMetrics.heightPixels) {
                resources.displayMetrics.widthPixels
            } else {
                resources.displayMetrics.heightPixels
            }
        val density = resources.displayMetrics.density
        val size = minSize / density
        var halfSize = size / 2
        if (halfSize >= 350) {
            halfSize = defaultMinSize
        }
        val newSize = halfSize * density
        withSize = newSize.toInt()
        heightSize = newSize.toInt()
        Log.d(
            javaClass.name,
            "withSize: $withSize, heightSize: $heightSize Density ${resources.displayMetrics.density} W:${resources.displayMetrics.widthPixels} H:${resources.displayMetrics.heightPixels} Size: $size HalfSize: $halfSize"
        )
    }

    var lastTouchY = 0f
    var lastTouchX = 0f
    var lastTouchOutY = 0f
    var lastTouchOutX = 0f
    val fontSize = 14
    val posX = (resources.displayMetrics.density * 14).toInt()
    val posY = (resources.displayMetrics.density * 40).toInt()
    val reduce = (resources.displayMetrics.density * 20)
    var lastYTitle = ((resources.displayMetrics.density * fontSize )+ 20)
    val posXTitle = posX + withSize + 5f
    var incrementYTitle = (resources.displayMetrics.density * fontSize) + 5

    private val draws = mutableListOf<PiecePie>()

        override fun onTouchEvent(event: MotionEvent?): Boolean {
            val x = event?.x
            val y = event?.y
            when(event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (x != null && y != null) {
                        draws.map {
                            RectF(
                                (posX + reduce),
                                (posY + reduce),
                                (posX + (withSize - reduce)),
                                (posY + (heightSize - reduce))
                            )
                        }.filter { it.contains(x!!, y!!) }.let {
                            if (it.isNotEmpty()) {
                                it.forEach {
                                    lastTouchX = x
                                    lastTouchY = y
                                }
                                lastTouchOutX = 0f
                                lastTouchOutY = 0f
                            } else {
                                lastTouchX = 0f
                                lastTouchY = 0f
                                lastTouchOutX = x
                                lastTouchOutY = y
                            }
                        }
                        invalidate()
                    }
                }
            }
            return true
    }

    private val drawable : ShapeDrawable = kotlin.run {
        contentDescription = "customdraw"

        ShapeDrawable(OvalShape()).apply {
            Log.d(javaClass.name,"withSize: $withSize, heightSize: $heightSize Density ${resources.displayMetrics.density } W:${resources.displayMetrics.widthPixels } H:${resources.displayMetrics.heightPixels }")
            paint.color = context.resources.getColor(R.color.background)
            setBounds(posX,posY,posX +withSize, posY + heightSize)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getColor(color:Int):Int{
        val cColor = context.getColor(color)
        return Color.valueOf(cColor).toArgb()
    }

       @RequiresApi(Build.VERSION_CODES.R)
       override fun onDraw(canvas:Canvas){
           drawable.draw(canvas)
           val bitMap = drawable.toBitmap(500, 500)
           lastYTitle = ((resources.displayMetrics.density * fontSize) + 20)
           val total = draws.sumOf { it.value }
           var start = 0f
           val colorTotal = getColor(R.color.text)
           drawTotal("Total: ", total, colorTotal, canvas)
           var selected: PiecePie? = null
           draws.forEach {
               val paint = getPaint(it.color)
               val end = calculate(calculatePercent(it.value, total))
               drawPieceOfPie(start, end, paint, canvas)
               drawTitle(it.title, it.value, paint, canvas, colorTotal)

               getSelected(start, end, it)?.let{
                   selected = it
               }

               if(isSelectedPiece()){
                   selected = it.copy(start=start, end = end)
               }

               start += end

           }
           drawSelected(selected, canvas, total)
       }

    private fun getSelected(start:Float,end:Float,piecePie:PiecePie):PiecePie?{
        if (lastTouchX > 0 && lastTouchY > 0) {
            val center = withSize/2
            val x = posX+reduce+center
            val y = posY+reduce+center
            val touchAngle = Math.toDegrees(atan2((y-lastTouchY).toDouble(),(x-lastTouchX).toDouble())).toFloat()
            val normalizer = touchAngle + 180
            Log.d(javaClass.name,"normalizer: $normalizer start: $start end: $end touchAngle: $touchAngle ${normalizer in start..start+end}")
            if (normalizer in start..start+end) {
                return piecePie.copy(start=start, end = end).also { Log.d(javaClass.name,"selected $it") }
            }
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun drawSelected(selected:PiecePie?,canvas: Canvas,total:Double){
        if(selected != null){
            Log.d(javaClass.name,"selected: $selected")
            val paint = getPaint(Color.YELLOW)
            val paint2 = getPaint(Color.BLACK)
            val color = getColor(R.color.green_dark_trans)
            val paint3 = getPaint(color)
            val text = getColor(R.color.text)
            val paint4 = getPaint(text)
            paint4.strokeWidth = 0.5f
            paint2.style = Paint.Style.STROKE
            paint2.strokeWidth = 4f
            canvas.drawCircle((posX+(withSize/2)).toFloat(),(posY+(withSize/2)).toFloat(),(withSize/2).toFloat(),paint3)
            drawPieceOfPie(selected?.start!!,selected?.end!!,paint,canvas)
            drawPieceOfPie(selected?.start!!,selected?.end!!,paint2,canvas)
            val percent = calculatePercent(selected?.value!!,total).toBigDecimal().round(
                MathContext(5)
            )
            val value= DecimalFormat("#,###.###").format(selected?.value).toString()
            canvas.drawText(" ${selected?.title} ", posX.toFloat(), posY.toFloat(), paint4)
            canvas.drawText(" $ $value ", posX.toFloat(), (posY + incrementYTitle).toFloat(), paint4)
            canvas.drawText("  $percent %", posX.toFloat(), (posY + (2*incrementYTitle)).toFloat (), paint4)
        }
    }

    private fun getPaint(color:Int,strokeWith:Float = 1f):Paint{
        val density = resources.displayMetrics.density
        val paint = Paint()
        paint.strokeWidth = strokeWith
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
        if(draws.filter { it.title == title }.isEmpty()){
            draws.add(piecePie)
        }
    }

    private fun isSelectedPiece():Boolean{
        val spaces = if(withReal < 1000 && resources.displayMetrics.density > 2.0){
            2
        }else{
            1
        }
        Log.d(javaClass.name,"spaces: $spaces lastTouchOutX: $lastTouchOutX lastTouchOutY: $lastTouchOutY lastTouchX: $posXTitle lastTouchY: $lastYTitle increment $incrementYTitle"
        +" $lastTouchOutX in $posXTitle .. ${posXTitle + squad}  "
            +"$lastTouchOutY in ${lastYTitle - (incrementYTitle * spaces)} .. $lastYTitle  "
        )
        if(lastTouchOutX > 0 && lastTouchOutY > 0) {
            if(lastTouchOutX in posXTitle .. posXTitle + squad &&
                lastTouchOutY in lastYTitle - (incrementYTitle * spaces) .. lastYTitle ){
                return true
            }
        }
        return false
    }

    private fun drawTitle(title:String,value:Double,paint:Paint,canvas:Canvas,color:Int){

        canvas.drawRect(
            posXTitle,
            lastYTitle + incrementYTitle,
            posXTitle + squad,
            lastYTitle + 10,
            paint
        )
        if(withReal < 1000 && resources.displayMetrics.density > 2.0){
            canvas.drawText(
                title,
                posXTitle + squad + 5,
                lastYTitle + incrementYTitle,
                getPaint(color,0.5F)
            )
            canvas.drawText("$ ${NumbersUtil.toString(value)}",posXTitle + incrementYTitle,lastYTitle+(2*incrementYTitle),getPaint(color))
            lastYTitle += (2 * incrementYTitle)
        }else {
            canvas.drawText(
                "$title $ ${NumbersUtil.toString(value)}",
                posXTitle + squad + 5,
                lastYTitle + incrementYTitle,
                getPaint(color,0.5f)
            )
            lastYTitle += (1 * incrementYTitle)
        }
    }

    private fun drawTotal(title:String,value:Double,color:Int,canvas:Canvas){
        canvas.drawText(
            "$title $ ${NumbersUtil.toString(value)}",
            (resources.displayMetrics.density * fontSize),
            (resources.displayMetrics.density * fontSize).toFloat(),
            getPaint(color,0.5f)
        )
    }

    private fun drawPieceOfPie(start:Float,end:Float,paint:Paint,canvas:Canvas){
        val rect = RectF(
            (posX + reduce),
            (posY + reduce),
            (posX + (withSize - reduce)),
            (posY + (heightSize - reduce))
        )
        canvas.drawArc(rect,start,end,true,paint)
    }


    fun clear(){
        draws.clear()
        lastYTitle = 10f
    }
}