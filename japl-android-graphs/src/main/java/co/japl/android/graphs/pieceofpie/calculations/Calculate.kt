package co.japl.android.graphs.pieceofpie.calculations

class Calculate {

    fun calculatePercent(valuePiece:Double,totalValue:Double):Float{
        if (totalValue == 0.0) return 0.0f
        val res = ((valuePiece * 100) / totalValue).toFloat()
        return if (res.isNaN() || res.isInfinite()) 0.0f else res
    }

    fun calculate(percent:Float):Float{
        if (percent.isNaN() || percent.isInfinite()) return 0.0f
        return (percent * 360) / 100
    }

}