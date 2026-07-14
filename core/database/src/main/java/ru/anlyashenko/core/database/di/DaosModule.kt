package ru.anlyashenko.core.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.anlyashenko.core.database.AppDatabase
import ru.anlyashenko.core.database.dao.CategoryDAO
import ru.anlyashenko.core.database.dao.ItemDAO

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {
    @Provides
    fun provideItemDAO(database: AppDatabase): ItemDAO {
        return database.itemDao()
    }

    @Provides
    fun provideCategoryDAO(database: AppDatabase): CategoryDAO {
        return database.categoryDao()
    }
}
