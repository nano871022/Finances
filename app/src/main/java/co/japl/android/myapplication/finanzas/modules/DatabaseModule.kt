package co.japl.android.myapplication.finanzas.modules

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideSQLiteDatabase(helper: SQLiteOpenHelper): SQLiteDatabase {
        return helper.writableDatabase
    }
}
