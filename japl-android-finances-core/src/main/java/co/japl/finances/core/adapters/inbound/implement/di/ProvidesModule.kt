package co.japl.finances.core.adapters.inbound.implement.di

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import co.com.japl.ui.impls.SMSObservable
import co.com.japl.ui.interfaces.ISMSObservablePublicher
import co.com.japl.ui.interfaces.ISMSObservableSubscriber
import co.com.japl.ui.interfaces.ISMSObserver
import co.com.japl.finances.iports.annotations.IObservers
import co.japl.android.finances.services.cache.impl.QuoteCreditCardCache
import co.japl.android.finances.services.cache.impl.SimulatorCreditCache
import co.japl.android.finances.services.cache.interfaces.IQuoteCreditCardCache
import co.japl.android.finances.services.cache.interfaces.ISimulatorCreditCache
import co.japl.android.finances.services.dao.interfaces.IQuoteCreditCardDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProvidesModule {

    @Provides
    fun provideMutableStateBooleanOf(): MutableState<Boolean> {
        return mutableStateOf(false)
    }

    @Singleton
    @Provides
    fun provideSMSObservable(): SMSObservable {
        return SMSObservable()
    }

    @Provides
    fun provideSMSObservablePublisher(sms: SMSObservable): ISMSObservablePublicher = sms

    @Provides
    fun provideSMSObservableSubscriber(sms: SMSObservable): ISMSObservableSubscriber = sms

    @Singleton
    @Provides
    fun provideBoughtCache(implement: IQuoteCreditCardDAO): IQuoteCreditCardCache {
        return QuoteCreditCardCache(implement)
    }

    @Singleton
    @Provides
    fun provideSimulatorCache(): ISimulatorCreditCache {
        return SimulatorCreditCache()
    }

    @IntoMap
    @IObservers(value = co.com.japl.module.creditcard.impl.SMSObserver::class)
    @Provides
    fun provideSMSObserverCreditCardModule( smsSvc: co.com.japl.finances.iports.inbounds.creditcard.ISMSCreditCardPort,subscriber: ISMSObservableSubscriber,ccSvc:co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort,msmSvc:co.com.japl.finances.iports.inbounds.creditcard.ISMSCreditCardPort,svc:co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtSmsPort):ISMSObserver{
        return co.com.japl.module.creditcard.impl.SMSObserver(smsSvc,subscriber,ccSvc,svc,msmSvc)
    }

    @IntoMap
    @IObservers(value = co.com.japl.module.paid.impl.SMSObserver::class)
    @Provides
    fun provideSMSObserverPaidModule(smsSvc: co.com.japl.finances.iports.inbounds.paid.ISMSPaidPort, subscriber: ISMSObservableSubscriber, accountSvc: co.com.japl.finances.iports.inbounds.inputs.IAccountPort, svc: co.com.japl.finances.iports.inbounds.paid.ISmsPort): ISMSObserver {
        return co.com.japl.module.paid.impl.SMSObserver(smsSvc, subscriber, accountSvc, svc)
    }
}
