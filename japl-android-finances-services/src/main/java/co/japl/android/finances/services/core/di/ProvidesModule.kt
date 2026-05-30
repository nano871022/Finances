package co.japl.android.finances.services.core.di

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import co.japl.android.finances.services.DB.connections.ConnectDB
import co.com.japl.ui.Prefs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProvidesModule {

    @Singleton
    @Provides
    fun provideConnectDB(@ApplicationContext context:Context): ConnectDB {
        return ConnectDB(context)
    }

    @Singleton
    @Provides
    fun provideSQLiteOpenHelper(connectDB: ConnectDB): SQLiteOpenHelper = connectDB

    @Singleton
    @Provides
    fun provideSQLiteDatabase(helper: SQLiteOpenHelper): SQLiteDatabase {
        return helper.writableDatabase
    }

    @Singleton
    @Provides
    fun providePrefs(@ApplicationContext context: Context): Prefs {
        return Prefs(context)
    }
}
