package co.japl.android.myapplication.finanzas.bussiness.interfaces

import java.math.BigDecimal

interface IAdditionalCreditSvc {

    fun updateValue(id:Int,value:BigDecimal):Boolean
}