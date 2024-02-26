package co.japl.android.finances.services.interfaces

import co.japl.android.finances.services.dto.PeriodDTO

interface IGetPeriodsServices {
    fun getPeriods(creaditCardId:Int):List<PeriodDTO>
}