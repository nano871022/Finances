package co.japl.android.myapplication.finanzas.bussiness.interfaces

import co.japl.android.myapplication.finanzas.bussiness.DTO.PeriodDTO

interface IGetPeriodsServices {
    fun getPeriods(creaditCardId:Int):List<PeriodDTO>
}