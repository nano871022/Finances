package co.japl.android.myapplication.finanzas.graphs.pieceofpie.calculations

class Calculate {

    fun calculatePercent(valuePiece:Double,totalValue:Double):Float{
        return ((valuePiece * 100) / totalValue).toFloat()
    }

    fun calculate(percent:Float):Float{
        return (percent * 360) / 100
    }

}