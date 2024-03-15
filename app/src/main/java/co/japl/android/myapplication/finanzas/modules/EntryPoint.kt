package co.japl.android.myapplication.finanzas.modules

import android.app.Application
import co.com.japl.finances.iports.inbounds.common.IDifferQuotesPort
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.lists.IBoughtListPort
import co.com.japl.finances.iports.inbounds.recap.IRecapPort
import co.japl.android.myapplication.bussiness.interfaces.ITaxSvc
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface EntryPoint {
    fun getBoughtCreditCardSvc():IBoughtListPort

    fun getApplication():Application

    fun getRecapSvc():IRecapPort

    fun getTaxSvc():ITaxSvc

    fun getCreditCardSvc():ICreditCardPort

    fun getDifferInstallmentSvc():IDifferQuotesPort
}