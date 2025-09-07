package co.japl.android.graphs.pieceofpie

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.graphics.alpha
import co.japl.android.graphs.R
import co.japl.android.graphs.colors.Colors
import co.japl.android.graphs.interfaces.IGraph
import co.japl.android.graphs.pieceofpie.calculations.Calculate
import co.japl.android.graphs.pojo.PiecePie
import co.com.japl.ui.utils.NumbersUtil
import java.math.BigDecimal
import java.math.MathContext
import java.text.DecimalFormat
import kotlin.math.atan2

class PieceOfPie constructor(val context:Context):IGraph{

    val fontSize = 14
    val posX = (context.resources.displayMetrics.density * 14).toInt()
    val posY = (context.resources.displayMetrics.density * 40).toInt()
    val defaultMinSize = 300F
    var withSize = defaultMinSize.toInt()
    var heightSize = defaultMinSize.toInt()
    var lastTouchY = 0f
    var lastTouchX = 0f
    var lastTouchOutY = 0f
    var lastTouchOutX = 0f
    val reduce = (context.resources.displayMetrics.density * 20)
    var lastYTitle = ((context.resources.displayMetrics.density * fontSize )+ 20)
    var incrementYTitle = (context.resources.displayMetrics.density * fontSize) + 5
    val squad = 10 * context.resources.displayMetrics.density
    val withReal = context.resources.displayMetrics.widthPixels / context.resources.displayMetrics.density
    private val draws = mutableListOf<PiecePie>()
    val colors = Colors(context,draws)
    val calculations = Calculate()

    init{
        val minSize =
            if (context.resources.displayMetrics.widthPixels < context.resources.displayMetrics.heightPixels) {
                context.resources.displayMetrics.widthPixels
            } else {
                context.resources.displayMetrics.heightPixels
            }
        val density = context.resources.displayMetrics.density
        val size = minSize / density
        var halfSize = size / 2
        if (halfSize >= 350) {
            halfSize = defaultMinSize
        }
        val newSize = halfSize * density
        withSize = newSize.toInt()
        heightSize = newSize.toInt()
    }

    val posXTitle = posX + withSize + 5f

    override fun drawBackground():ShapeDrawable{
        return ShapeDrawable(OvalShape()).apply {
            paint.color = context.resources.getColor(R.color.background,null)
            setBounds(posX,posY,posX +withSize, posY + heightSize)
        }
    }

    fun drawTotal(title:String,value:Double,color:Int,canvas: Canvas){
        canvas.drawText(
            "$title $ ${NumbersUtil.toString(value)}",
            (context.resources.displayMetrics.density * fontSize),
            (context.resources.displayMetrics.density * fontSize).toFloat(),
            getPaint(color,0.5f)
        )
    }

    private fun getPaint(color:Int,strokeWith:Float = 1f): Paint {
        val density = context.resources.displayMetrics.density
        val paint = Paint()
        paint.strokeWidth = strokeWith
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = color
        paint.textSize = density * fontSize
        return paint
    }

    private fun drawTitle(title:String,value:Double,paint:Paint,canvas:Canvas,color:Int){

        canvas.drawRect(
            posXTitle,
            lastYTitle + incrementYTitle,
            posXTitle + squad,
            lastYTitle + 10,
            paint
        )
        if(withReal < 1000 && context.resources.displayMetrics.density > 2.0){
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

    private fun drawPieceOfPie(start:Float,end:Float,paint:Paint,canvas:Canvas){
        val rect = RectF(
            (posX + reduce),
            (posY + reduce),
            (posX + (withSize - reduce)),
            (posY + (heightSize - reduce))
        )
        canvas.drawArc(rect,start,end,true,paint)
    }

    private fun getSelected(start:Float,end:Float,piecePie: PiecePie): PiecePie?{
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
        if(selected != null) {
            Log.d(javaClass.name, "selected: $selected")
            val paint = getPaint(Color.YELLOW)
            val paint2 = getPaint(Color.BLACK)
            val color = colors.getColor(R.color.green_dark_trans)
            val paint3 = getPaint(color)
            val text = colors.getColor(R.color.text)
            val paint4 = getPaint(text)
            paint4.strokeWidth = 0.5f
            paint2.style = Paint.Style.STROKE
            paint2.strokeWidth = 4f
            canvas.drawCircle(
                (posX + (withSize / 2)).toFloat(),
                (posY + (withSize / 2)).toFloat(),
                (withSize / 2).toFloat(),
                paint3
            )
            drawPieceOfPie(selected?.start!!, selected?.end!!, paint, canvas)
            drawPieceOfPie(selected?.start!!, selected?.end!!, paint2, canvas)

            descriptionPieceOfPie(
                selected?.title!!,
                selected?.value!!,
                total,
                canvas,
                paint4
            )
        }
    }

    private fun descriptionPieceOfPie(title:String,value:Double,total:Double,canvas: Canvas,paint4:Paint){
        val percent = calculations.calculatePercent(value!!,total).toBigDecimal().round(
            MathContext(5)
        )
        val value= DecimalFormat("#,###.###").format(value).toString()
        val longText = getLongText(title,value,percent)
        val longHigh = Paint().fontSpacing

        val color = colors.getColor(R.color.green_dark_trans_s)
        val paint = getPaint(color)
        Log.w(javaClass.name,"=== descriptionPieceOfPie longText: $longText longHigh: $longHigh posX: $posX posY: $posY incrementYTitle: $incrementYTitle withSize: $withSize Density: ${context.resources.displayMetrics.density}")
        canvas.drawRoundRect(posX.toFloat()
            , posY.toFloat() - (context.resources.displayMetrics.density*longHigh)
            ,posX + (context.resources.displayMetrics.density * longText * 1.25).toFloat()
            , posY.toFloat() + (2* incrementYTitle) +(context.resources.displayMetrics.density *longHigh)
            ,10f
            ,10f
            ,paint)

        canvas.drawText(" $title ", posX.toFloat(), posY.toFloat(), paint4)
        canvas.drawText(" $ $value ", posX.toFloat(), (posY + incrementYTitle).toFloat(), paint4)
        canvas.drawText("  $percent %", posX.toFloat(), (posY + (2*incrementYTitle)).toFloat (), paint4)
    }

    private fun getLongText(title:String,value:String,percent:BigDecimal):Float{
        val paint = Paint()
        val value1 = paint.measureText(title)
        val value2 = paint.measureText("$ $value ")
        val value3 = paint.measureText("$percent % ")
        if(value1 > value2 && value1 > value3){
            return value1
        }else if(value2 > value1 && value2 > value3){
            return value2
        }else if(value3 > value1 && value3 > value2){
            return value3
        }else if(value1 > value2 && value1 == value3){
            return value1
        }else if(value2 > value1 && value2 == value3){
            return value2
        }else if(value1 > value3 && value1 == value2){
            return value1
        }
        return 0f
    }

    private fun isSelectedPiece():Boolean{
        val spaces = if(withReal < 1000 && context.resources.displayMetrics.density > 2.0){
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

    override fun validateTouch(x:Float,y:Float){
        draws.map {
            RectF(
                (posX + reduce),
                (posY + reduce),
                (posX + (withSize - reduce)),
                (posY + (heightSize - reduce))
            )
        }.filter { it.contains(x, y) }.let {
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
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun drawing(canvas: Canvas){
        lastYTitle = ((context.resources.displayMetrics.density * fontSize) + 20)
        val total = draws.sumOf { it.value }
        var start = 0f
        val colorTotal = colors.getColor(R.color.text)
        drawTotal("Total: ", total, colorTotal, canvas)
        var selected: PiecePie? = null
        draws.forEach {
            val paint = getPaint(it.color)
            val end = calculations.calculate(calculations.calculatePercent(it.value, total))
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun addPiece(title:String, value:Double){
        val color = colors.getColor()
        val piecePie = PiecePie(color,title,value)
        if(draws.filter { it.title == title }.isEmpty()){
            draws.add(piecePie)
        }
    }

    override fun clear(){
        draws.clear()
        lastYTitle = 10f
    }

}