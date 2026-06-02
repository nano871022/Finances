package co.japl.android.myapplication.finanzas.modules

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import co.com.japl.ui.Prefs
import co.com.japl.finances.iports.observables.ISMSObservableSubscriber
import co.com.japl.finances.iports.observables.ISMSObserver
import co.com.japl.finances.iports.inbounds.common.ISMSBoadcastReceiver
import co.com.japl.finances.iports.inbounds.common.IGoogleDriveService
import co.com.japl.finances.iports.inbounds.common.IGoogleSignInService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import dagger.multibindings.ClassKey
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AbstractModule {

    @Binds
    abstract fun bindContext(@ApplicationContext context:Context):Context

    @Binds
    abstract fun bindSMSBroadcastReceiver(sms: co.japl.android.finances.services.core.SMS): ISMSBoadcastReceiver

    @Binds
    abstract fun bindGoogleDriveService(impl: co.japl.android.finances.services.implement.GoogleDriveServiceImpl): IGoogleDriveService

    @Binds
    abstract fun bindGoogleSignInService(impl: co.japl.android.finances.services.implement.GoogleLoginService): IGoogleSignInService

    companion object {
        @Singleton
        @Provides
        fun providePrefs(@ApplicationContext context: Context): Prefs {
            return Prefs(context)
        }

        @IntoMap
        @ClassKey(co.com.japl.module.creditcard.impl.SMSObserver::class)
        @Provides
        fun provideSMSObserverCreditCardModule( smsSvc: co.com.japl.finances.iports.inbounds.creditcard.ISMSCreditCardPort,subscriber: ISMSObservableSubscriber,ccSvc:co.com.japl.finances.iports.inbounds.creditcard.ICreditCardPort,msmSvc:co.com.japl.finances.iports.inbounds.creditcard.ISMSCreditCardPort,svc:co.com.japl.finances.iports.inbounds.creditcard.bought.IBoughtSmsPort):ISMSObserver{
            return co.com.japl.module.creditcard.impl.SMSObserver(smsSvc,subscriber,ccSvc,svc,msmSvc)
        }

        @IntoMap
        @ClassKey(co.com.japl.module.paid.impl.SMSObserver::class)
        @Provides
        fun provideSMSObserverPaidModule(smsSvc: co.com.japl.finances.iports.inbounds.paid.ISMSPaidPort, subscriber: ISMSObservableSubscriber, accountSvc: co.com.japl.finances.iports.inbounds.inputs.IAccountPort, svc: co.com.japl.finances.iports.inbounds.paid.ISmsPort): ISMSObserver {
            return co.com.japl.module.paid.impl.SMSObserver(smsSvc, subscriber, accountSvc, svc)
        }

        @Provides
        fun provideMutableStateBooleanOf(): MutableState<Boolean> {
            return mutableStateOf(false)
        }


        @Provides
        fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
    }
}
