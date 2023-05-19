package co.japl.android.myapplication.finanzas.bussiness.interfaces

import co.japl.android.myapplication.finanzas.enums.KindOfTaxEnum

interface IKindOfTaxSvc {
    fun getNM(value:Double,kindOf: KindOfTaxEnum):Double
}