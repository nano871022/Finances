package co.japl.android.myapplication.finanzas.bussiness.interfaces

import co.japl.android.myapplication.finanzas.utils.KindOfTaxEnum

interface IKindOfTaxSvc {
    fun getNM(value:Double,kindOf:KindOfTaxEnum):Double
}