package ru.anlyashenko.core.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import ru.anlyashenko.core.common.di.IoDispatcher
import ru.anlyashenko.core.database.AppDatabase
import ru.anlyashenko.core.database.DatabaseCallback
import ru.anlyashenko.core.database.dao.ItemDAO
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        databaseProvider: Provider<AppDatabase>,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app-database"
    )
        .addCallback(DatabaseCallback(databaseProvider, ioDispatcher))
        .fallbackToDestructiveMigration(true)
        .build()
}
