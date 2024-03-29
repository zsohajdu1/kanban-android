package com.example.kanban_android.presentation.register

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.kanban_android.domain.services.base.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

    val uiState = mutableStateOf(RegisterUiState())

    data class RegisterUiState (
        val email: String = "",
        val password: String = "",
        val repeatPassword: String = ""
    )
}