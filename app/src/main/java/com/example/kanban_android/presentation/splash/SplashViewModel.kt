package com.example.kanban_android.presentation.splash

import com.example.kanban_android.AUTH_SCREEN
import com.example.kanban_android.HOME_SCREEN
import com.example.kanban_android.domain.services.base.AuthService
import com.example.kanban_android.domain.services.base.LogService
import com.example.kanban_android.presentation.KanbanAndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authService: AuthService,
    private val logService: LogService
) : KanbanAndroidViewModel(logService) {
    fun onAppStart(openScreen: (String) -> Unit) {
        if (authService.hasUser) openScreen(HOME_SCREEN)
        else openScreen(AUTH_SCREEN)
    }

}