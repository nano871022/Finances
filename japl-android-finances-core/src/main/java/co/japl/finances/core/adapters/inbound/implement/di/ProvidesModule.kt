package co.japl.finances.core.adapters.inbound.implement.di

import co.com.japl.ui.impls.SMSObservable
import co.com.japl.finances.iports.observables.ISMSObservablePublicher
import co.com.japl.finances.iports.observables.ISMSObservableSubscriber
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProvidesModule {



    @Singleton
    @Provides
    fun provideSMSObservable(): SMSObservable {
        return SMSObservable()
    }
}
