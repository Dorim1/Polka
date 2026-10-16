package ru.anlyashenko.data_test

import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import ru.anlyashenko.core.data.di.DataModule
import ru.anlyashenko.core.data.repository.InventoryRepository
import ru.anlyashenko.data_test.repository.FakeInventoryRepository

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class],
)
internal interface TestDataModule {
    @Binds
    fun bindsInventoryRepository(
        fakeInventoryRepository: FakeInventoryRepository
    ) : InventoryRepository
}
