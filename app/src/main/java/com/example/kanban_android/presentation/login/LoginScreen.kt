package com.example.kanban_android.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen(
    openScreenOnTop: (String) -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
    LoginScreenContent(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLoginClick = { viewModel.onLoginClick(openScreenOnTop) },
        onForgotPasswordClick = { viewModel.onForgotPasswordClick() }
    )

}

@Composable
fun LoginScreenContent(
    modifier: Modifier = Modifier,
    uiState: LoginViewModel.LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(60.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                modifier = modifier.fillMaxWidth(),
                singleLine = true,
                value = uiState.email,
                onValueChange = onEmailChange,
                label = { Text(text = "Email") }
            )
            TextField(
                modifier = modifier.fillMaxWidth(),
                singleLine = true,
                value = uiState.password,
                onValueChange = onPasswordChange,
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation()
            )
            Button(
                modifier = modifier.fillMaxWidth(),
                onClick = onLoginClick
            ) {
                Text("Login")
            }

            Button(
                modifier = modifier.fillMaxWidth(),
                onClick = onForgotPasswordClick
            ) {
                Text("Forgot password")
            }
        }
    }
}

