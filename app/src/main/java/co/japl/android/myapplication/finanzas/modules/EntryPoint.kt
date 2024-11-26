package co.japl.android.myapplication.finanzas.modules

import android.app.Activity
import android.app.Application
import co.com.japl.finances.iports.inbounds.common.IDifferQuotesPort
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ITaxPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.lists.IBoughtListPort
import co.com.japl.finances.iports.inbounds.recap.IRecapPort
import co.com.japl.module.creditcard.impl.SMSObserver
import co.com.japl.ui.interfaces.ISMSObservableSubscriber
import co.com.japl.ui.interfaces.ISMSObserver
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

    fun getInboundTaxPort():ITaxPort

    fun getCreditCardSvc():ICreditCardPort

    fun getDifferInstallmentSvc():IDifferQuotesPort

   // fun getAllImplementationSMSSubscriber():Set<ISMSObserver>
}