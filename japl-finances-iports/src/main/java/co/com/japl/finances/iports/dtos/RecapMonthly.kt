package co.com.japl.finances.iports.dtos

data class RecapMonthly(
    val current:RecapCurrentMonthly?,
    val last:RecapLastMonthly?,
    val graph:List<Pair<String,Double>>?
)
