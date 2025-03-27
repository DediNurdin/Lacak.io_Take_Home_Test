package com.dedinurdin.lacakiotakehometest.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dedinurdin.lacakiotakehometest.data.api.FleetApiService
import com.dedinurdin.lacakiotakehometest.data.api.MockFleetApi
import com.dedinurdin.lacakiotakehometest.data.repository.FleetRepository
import com.dedinurdin.lacakiotakehometest.viewmodel.FleetViewModel
import com.google.android.datatransport.runtime.dagger.Module
import com.google.android.datatransport.runtime.dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideFleetApiService(): FleetApiService = MockFleetApi()

    @Provides
    @Singleton
    fun provideFleetRepository(apiService: FleetApiService): FleetRepository = FleetRepository(apiService)

    @Provides
    @Singleton
    fun provideViewModelFactory(repository: FleetRepository): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FleetViewModel(repository) as T
            }
        }
    }
}