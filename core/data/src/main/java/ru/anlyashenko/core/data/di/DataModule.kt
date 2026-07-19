package ru.anlyashenko.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.anlyashenko.core.data.repository.InventoryRepository
import ru.anlyashenko.core.data.repository.OfflineFirstInventoryRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsInventoryRepository(
        inventoryRepository: OfflineFirstInventoryRepository,
    ): InventoryRepository
}
