package com.example.kanban_android.di

import com.example.kanban_android.domain.services.base.AuthService
import com.example.kanban_android.domain.repositories.base.BoardRepository
import com.example.kanban_android.domain.repositories.base.ColumnRepository
import com.example.kanban_android.domain.services.base.LogService
import com.example.kanban_android.domain.repositories.base.TaskRepository
import com.example.kanban_android.domain.repositories.base.WorkspaceRepository
import com.example.kanban_android.domain.services.impl.AuthServiceImpl
import com.example.kanban_android.domain.repositories.impl.BoardRepositoryImpl
import com.example.kanban_android.domain.repositories.impl.ColumnRepositoryImpl
import com.example.kanban_android.domain.services.impl.LogServiceImpl
import com.example.kanban_android.domain.repositories.impl.TaskRepositoryImpl
import com.example.kanban_android.domain.repositories.impl.WorkspaceRepositoryImpl
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

    @Binds
    @Singleton
    abstract fun bindLogService(
        logServiceImpl: LogServiceImpl
    ): LogService


}