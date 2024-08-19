package com.alvin.quiz.core.di.module

import com.alvin.quiz.core.service.AuthService
import com.alvin.quiz.core.service.StorageService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    @Provides
    @Singleton
    fun provideAuthService(): AuthService {
        return AuthService()
    }
    @Provides
    @Singleton
    fun provideStorageService(authService: AuthService): StorageService {
        return StorageService(authService)
    }
}