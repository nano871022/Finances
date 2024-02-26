package co.japl.android.finances.services.interfaces

import co.japl.android.finances.services.dto.GraphValuesResp
import java.time.LocalDate

interface IGraph {
    fun getValues(date:LocalDate):List<GraphValuesResp>
}