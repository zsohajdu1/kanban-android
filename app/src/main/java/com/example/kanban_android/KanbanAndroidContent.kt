package com.example.kanban_android

import android.content.res.Resources
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kanban_android.common.snackbar.SnackbarManager
import com.example.kanban_android.domain.model.Board
import com.example.kanban_android.domain.model.Task
import com.example.kanban_android.domain.model.Workspace
import com.example.kanban_android.presentation.login.LoginScreen
import com.example.kanban_android.presentation.auth.AuthScreen
import com.example.kanban_android.presentation.board.BoardScreen
import com.example.kanban_android.presentation.home.HomeScreen
import com.example.kanban_android.presentation.register.RegisterScreen
import com.example.kanban_android.presentation.splash.SplashScreen
import com.example.kanban_android.presentation.task.TaskScreen
import com.example.kanban_android.presentation.workspace.WorkspaceScreen
import com.example.kanban_android.ui.theme.KanbanandroidTheme
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KanbanAndroidContent() {
    KanbanandroidTheme {

        //val navController = rememberNavController()
        //val snackbarHostState = remember { SnackbarHostState() }
        //val scope = rememberCoroutineScope()

        val appState = rememberAppState()
        Surface(color = MaterialTheme.colorScheme.background) {
            Scaffold(
                snackbarHost = {
                    SnackbarHost(
                        hostState = appState.snackbarHostState,
                    )
                },
                content = { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavGraph(appState.navController)
                    }
                }
            )
        }
    }
}

@Composable
fun rememberAppState(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    navController: NavHostController = rememberNavController(),
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = LocalContext.current.resources,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(snackbarHostState, navController, snackbarManager, resources, coroutineScope) {
        KanbanAndroidAppState(
            snackbarHostState,
            navController,
            snackbarManager,
            resources,
            coroutineScope
        )
    }


@Composable
fun NavGraph(navController: NavHostController) {
    val gson = Gson()
    NavHost(
        navController,
        startDestination = SPLASH_SCREEN
    ) {
        composable(AUTH_SCREEN) {
            AuthScreen({ route: String -> navController.navigate(route) })
        }
        composable(LOGIN_SCREEN) {
            LoginScreen({ route: String ->
                navController.navigate(route) {
                    popUpTo(0) { inclusive = true }
                }
            })
        }
        composable(REGISTER_SCREEN) {
            RegisterScreen({ route: String ->
                navController.navigate(route) {
                    popUpTo(0) { inclusive = true }
                }
            })
        }

        composable(HOME_SCREEN) {
            HomeScreen(
                { route: String -> navController.navigate(route) },
                { route: String ->
                    navController.navigate(route) {
                        popUpTo(0) { inclusive = true }
                    }
                })
        }

        composable(SPLASH_SCREEN) {
            SplashScreen({ route: String ->
                navController.navigate(route) {
                    popUpTo(0) { inclusive = true }
                }
            })
        }
        composable("$WORKSPACE_SCREEN/{workspaceObject}") {
            val workspaceObject = it.arguments?.getString("workspaceObject")
            val workspace = gson.fromJson(workspaceObject, Workspace::class.java)
            WorkspaceScreen(
                { route: String -> navController.navigate(route) },
                { navController.popBackStack() },
                workspace = workspace
            )
        }
        composable("$BOARD_SCREEN/{boardObject}") {
            val boardObject = it.arguments?.getString("boardObject")
            val board = gson.fromJson(boardObject, Board::class.java)
            BoardScreen(
                { route: String -> navController.navigate(route) },
                { navController.popBackStack() },
                board = board
            )
        }
        composable("$TASK_SCREEN/{boardObject}/{taskObject}") {
            val boardObject = it.arguments?.getString("boardObject")
            val board = gson.fromJson(boardObject, Board::class.java)
            val taskObject = it.arguments?.getString("taskObject")
            val task = gson.fromJson(taskObject, Task::class.java)
            TaskScreen(
                { navController.popBackStack() },
                board = board,
                task = task
            )
        }
    }
}

