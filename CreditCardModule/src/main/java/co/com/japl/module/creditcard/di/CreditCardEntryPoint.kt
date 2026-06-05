package co.com.japl.module.creditcard.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.EntryPoint
import dagger.hilt.components.SingletonComponent
import java.time.YearMonth
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CreditCardEntryPoint {

    @Singleton
    @Provides
    fun bindYearMonth():YearMonth=YearMonth.now()

}
