package com.example.kanban_android.presentation.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SplashScreen(
    openScreenOnTop: (String) -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    LaunchedEffect(true) {
        viewModel.onAppStart(openScreenOnTop)

    }
}