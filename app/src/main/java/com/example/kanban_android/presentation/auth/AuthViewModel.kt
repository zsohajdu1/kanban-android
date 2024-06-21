package com.example.kanban_android.presentation.auth

import com.example.kanban_android.LOGIN_SCREEN
import com.example.kanban_android.REGISTER_SCREEN
import com.example.kanban_android.domain.services.base.LogService
import com.example.kanban_android.presentation.KanbanAndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val logService: LogService
) : KanbanAndroidViewModel(logService) {

    fun onRegisterClick(openScreen: (String) -> Unit) {
        openScreen(REGISTER_SCREEN)
    }

    fun onLoginClick(openScreen: (String) -> Unit) {
        openScreen(LOGIN_SCREEN)
    }
}