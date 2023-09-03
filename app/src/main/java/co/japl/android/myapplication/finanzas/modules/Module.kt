package co.japl.android.myapplication.finanzas.modules

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import co.japl.android.myapplication.bussiness.DB.connections.ConnectDB
import co.japl.android.myapplication.bussiness.impl.CreditCardImpl
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


}