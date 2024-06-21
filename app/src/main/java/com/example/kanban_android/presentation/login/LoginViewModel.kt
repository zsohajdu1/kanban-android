package com.example.kanban_android.presentation.login

import androidx.compose.runtime.mutableStateOf
import com.example.kanban_android.HOME_SCREEN
import com.example.kanban_android.common.ext.isValidEmail
import com.example.kanban_android.common.snackbar.SnackbarManager
import com.example.kanban_android.domain.services.base.AuthService
import com.example.kanban_android.domain.services.base.LogService
import com.example.kanban_android.presentation.KanbanAndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.kanban_android.R.string as AppText
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService,
    private val logService: LogService
) : KanbanAndroidViewModel(logService) {

    val uiState = mutableStateOf(LoginUiState())

    data class LoginUiState(
        val email: String = "",
        val password: String = ""
    )

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onLoginClick(openScreen: (String) -> Unit) {
        if (!uiState.value.email.isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
        } else if (uiState.value.password.isBlank()) {
            SnackbarManager.showMessage(AppText.empty_password_error)
        } else launchCatching {
            authService.authenticate(uiState.value.email, uiState.value.password)
            openScreen(HOME_SCREEN)
        }
    }

    fun onForgotPasswordClick() {
        if (!uiState.value.email.isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        } else launchCatching {
            authService.sendRecoveryEmail(uiState.value.email)
            SnackbarManager.showMessage(AppText.password_recovery)
        }
    }

}