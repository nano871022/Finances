package co.japl.android.myapplication.finanzas.modules

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.ISMSCreditCardPort
import co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtSmsPort
import co.com.japl.module.creditcard.impl.SMSObserver
import co.com.japl.ui.Prefs
import co.com.japl.ui.impls.SMSObservable
import co.com.japl.ui.interfaces.ISMSObservablePublicher
import co.com.japl.ui.interfaces.ISMSObservableSubscriber
import co.com.japl.ui.interfaces.ISMSObserver
import co.japl.android.finances.services.cache.impl.QuoteCreditCardCache
import co.japl.android.finances.services.cache.impl.SimulatorCreditCache
import co.japl.android.finances.services.cache.interfaces.ISimulatorCreditCache
import co.japl.android.finances.services.dao.interfaces.IQuoteCreditCardDAO
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.finanzas.ApplicationInitial
import co.japl.android.myapplication.finanzas.annotations.IObservers
import co.japl.android.myapplication.finanzas.controller.SMS
import co.japl.android.myapplication.finanzas.interfaces.ISMSBoadcastReceiver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {
    @Singleton
    @Provides
    fun provideConnectDB(@ApplicationContext context:Context): SQLiteOpenHelper {
        return ConnectDB(context)
    }

    @Provides
    fun provideMutableStateBooleanOf():MutableState<Boolean>{
        return mutableStateOf(false)
    }


    @Singleton
    @Provides
    fun getPrefs():Prefs{
        return  ApplicationInitial.prefs
    }

    @Singleton
    @Provides
    fun getSMSObserver():SMSObservable{
        return SMSObservable()
    }

    @Singleton
    @Provides
    fun getSMSBroadcastReceiver(@ApplicationContext context:Context,publisher:ISMSObservablePublicher):ISMSBoadcastReceiver{
        return SMS(publisher,context)
    }

    @Singleton
    @Provides
    fun getDAOBoughtcache(implement:IQuoteCreditCardDAO):co.japl.android.finances.services.cache.interfaces.IQuoteCreditCardCache{
        return QuoteCreditCardCache(implement)
    }


    @Singleton
    @Provides
    fun getDAOSimulatorCache(): ISimulatorCreditCache{
        return SimulatorCreditCache()
    }

    @IntoMap
    @IObservers(value = SMSObserver::class)
    @Provides
    fun getSMSObserverCreditCardModule( smsSvc: ISMSCreditCardPort,subscriber: ISMSObservableSubscriber,ccSvc:ICreditCardPort,msmSvc:ISMSCreditCardPort,svc:IBoughtSmsPort):ISMSObserver{
        return SMSObserver(smsSvc,subscriber,ccSvc,svc,msmSvc)
    }




}