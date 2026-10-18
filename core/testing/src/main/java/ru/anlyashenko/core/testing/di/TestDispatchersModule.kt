package ru.anlyashenko.core.testing.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestDispatcher
import ru.anlyashenko.core.common.di.DefaultDispatcher
import ru.anlyashenko.core.common.di.DispatchersModule
import ru.anlyashenko.core.common.di.IoDispatcher
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DispatchersModule::class]
)
internal object TestDispatchersModule {
    @Provides
    @IoDispatcher
    fun providesIODispatcher(testDispatcher: TestDispatcher): CoroutineDispatcher = testDispatcher

    @Provides
    @DefaultDispatcher
    fun providesDefaultDispatcher(
        testDispatcher: TestDispatcher,
    ): CoroutineDispatcher = testDispatcher
}
