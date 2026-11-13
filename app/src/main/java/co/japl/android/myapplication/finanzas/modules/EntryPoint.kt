package co.japl.android.myapplication.finanzas.modules

import android.app.Application
import co.com.japl.finances.iports.inbounds.common.IDifferQuotesPort
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.IEmailCreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ISimulatorCreditVariablePort
import co.com.japl.finances.iports.inbounds.common.IGoogleDriveService
import co.com.japl.finances.iports.inbounds.paid.IEmailPaidPort
import co.com.japl.finances.iports.inbounds.creditcard.ITaxPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtSmsPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.lists.IBoughtListPort
import co.com.japl.finances.iports.inbounds.recap.IRecapPort
import co.japl.android.finances.services.DB.connections.ConnectDB
import co.com.japl.ui.Prefs
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

   fun getSimulatorVariablePort(): ISimulatorCreditVariablePort

   fun getEmailCreditCardPort(): IEmailCreditCardPort

   fun getEmailPaidPort(): IEmailPaidPort

   fun getInboundBoughtSmsPort(): IBoughtSmsPort

   fun getGoogleDriveSvc(): IGoogleDriveService

   fun getPrefs(): Prefs

   fun getConnectDB(): ConnectDB
}