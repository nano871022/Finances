package co.japl.android.myapplication.finanzas.modules

import android.content.Context
import co.com.japl.ui.Prefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PrefsModule {

    @Provides
    @Singleton
    fun providePrefs(@ApplicationContext context: Context): Prefs {
        return Prefs(context)
    }
}
