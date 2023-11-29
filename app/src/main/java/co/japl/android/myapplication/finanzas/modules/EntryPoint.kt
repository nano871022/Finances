package co.japl.android.myapplication.finanzas.modules

import co.com.japl.finances.iports.inbounds.creditcard.bought.lists.IBoughtListPort
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface EntryPoint {
    fun getBoughtCreditCardSvc():IBoughtListPort
}