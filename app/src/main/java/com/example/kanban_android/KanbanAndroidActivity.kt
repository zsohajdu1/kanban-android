package com.example.kanban_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import com.example.kanban_android.ui.theme.KanbanandroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class KanbanAndroidActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KanbanandroidTheme {
                NavGraph()
            }
        }
    }

    @Composable
    fun NavGraph() {
        // TODO implement
    }
}