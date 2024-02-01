package com.starrydot.starrycosmo.di

import android.content.Context
import com.starrydot.starrycosmo.domain.device.DeviceRepository
import com.starrydot.starrycosmo.repository.device.DeviceRepositoryImplementation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Singleton
    @Provides
    fun provideDeviceRepository(@ApplicationContext context: Context): DeviceRepository {
        return DeviceRepositoryImplementation(context)
    }
}
