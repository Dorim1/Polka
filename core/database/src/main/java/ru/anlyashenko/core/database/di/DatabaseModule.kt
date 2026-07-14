package ru.anlyashenko.core.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import ru.anlyashenko.core.database.AppDatabase
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
        itemDaoProvider: Provider<ItemDAO>
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app-database"
    )
//        .addCallback(DatabaseCallback(itemDaoProvider, CoroutineScope(Dispatchers.IO))) todo: add callback
        .fallbackToDestructiveMigration(true)
        .build()
}
