package co.japl.android.myapplication.finanzas.bussiness.interfaces

import co.japl.android.myapplication.finanzas.bussiness.response.GraphValuesResp
import java.time.LocalDate

interface IGraph {
    fun getValues(date:LocalDate):List<GraphValuesResp>
}