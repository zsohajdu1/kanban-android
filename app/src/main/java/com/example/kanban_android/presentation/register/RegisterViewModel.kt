package com.example.kanban_android.presentation.register

import androidx.compose.runtime.mutableStateOf
import com.example.kanban_android.HOME_SCREEN
import com.example.kanban_android.R.string as AppText
import com.example.kanban_android.common.ext.isValidEmail
import com.example.kanban_android.common.ext.isValidPassword
import com.example.kanban_android.common.snackbar.SnackbarManager
import com.example.kanban_android.domain.services.base.AuthService
import com.example.kanban_android.domain.services.base.LogService
import com.example.kanban_android.presentation.KanbanAndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authService: AuthService,
    private val logService: LogService
) : KanbanAndroidViewModel(logService) {

    val uiState = mutableStateOf(RegisterUiState())

    data class RegisterUiState(
        val email: String = "",
        val password: String = "",
        val repeatPassword: String = ""
    )

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onRepeatPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(repeatPassword = newValue)
    }

    fun onRegisterClick(openScreen: (String) -> Unit) {
        if (!uiState.value.email.isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
        }

        else if (!uiState.value.password.isValidPassword()) {
            SnackbarManager.showMessage(AppText.password_error)
        }

        else if (uiState.value.password != uiState.value.repeatPassword) {
            SnackbarManager.showMessage(AppText.password_match_error)
        }

        else launchCatching {
            authService.signUp(uiState.value.email, uiState.value.password)
            openScreen(HOME_SCREEN)
        }


    }

}