package com.example.kanban_android.presentation.login

import androidx.lifecycle.ViewModel
import com.example.kanban_android.domain.services.base.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authService: AuthService
) : ViewModel() {

}