package com.example.kanban_android.presentation.register

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
fun RegisterScreen(
    openScreenOnTop: (String) -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
    RegisterScreenContent(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onRepeatPasswordChange = viewModel::onRepeatPasswordChange,
        onRegisterClick = { viewModel.onRegisterClick(openScreenOnTop) }
    )
}

@Composable
fun RegisterScreenContent(
    modifier: Modifier = Modifier,
    uiState: RegisterViewModel.RegisterUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRepeatPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit
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
            horizontalAlignment = Alignment.CenterHorizontally,

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
            TextField(
                modifier = modifier.fillMaxWidth(),
                singleLine = true,
                value = uiState.repeatPassword,
                onValueChange = onRepeatPasswordChange,
                label = { Text(text = "Repeat password") },
                visualTransformation = PasswordVisualTransformation()
            )
            Button(
                modifier = modifier.fillMaxWidth(),
                onClick = onRegisterClick
            ) {
                Text("Register")
            }
        }
    }
}

