package co.japl.android.myapplication.finanzas.modules

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import co.com.japl.ui.Prefs
import co.japl.android.finances.services.cache.impl.QuoteCreditCardCache
import co.japl.android.finances.services.dao.interfaces.IQuoteCreditCardDAO
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.impl.CreditCardImpl
import co.japl.android.myapplication.finanzas.ApplicationInitial
import co.japl.android.myapplication.finanzas.bussiness.interfaces.ICreditCardSvc
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {
    @Singleton
    @Provides
    fun provideConnectDB(@ApplicationContext context:Context): SQLiteOpenHelper {
        return ConnectDB(context)
    }

    @Singleton
    @Provides
    fun getPrefs():Prefs{
        return  ApplicationInitial.prefs
    }

    @Singleton
    @Provides
    fun getDAOBoughtcache(implement:IQuoteCreditCardDAO):co.japl.android.finances.services.cache.interfaces.IQuoteCreditCardCache{
        return QuoteCreditCardCache(implement)
    }


}