package co.japl.android.finances.services.interfaces

import co.japl.android.finances.services.enums.KindOfTaxEnum

interface IKindOfTaxSvc {
    fun getNM(value:Double,kindOf: KindOfTaxEnum):Double
}