package com.example.kanban_android.di

import com.example.kanban_android.domain.services.base.AuthService
import com.example.kanban_android.domain.services.impl.AuthServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Binds
    @Singleton
    abstract fun bindAuthService(
        authServiceImpl: AuthServiceImpl
    ): AuthService

}