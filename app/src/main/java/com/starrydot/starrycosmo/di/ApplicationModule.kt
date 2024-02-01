package com.starrydot.starrycosmo.di

import com.starrydot.starrycosmo.domain.device.DeviceRepository
import com.starrydot.starrycosmo.repository.device.DeviceRepositoryImplementation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Singleton
    @Provides
    fun provideDeviceRepository(): DeviceRepository {
        return DeviceRepositoryImplementation()
    }
}
