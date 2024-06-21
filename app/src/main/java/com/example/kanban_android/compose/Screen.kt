package com.example.kanban_android.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kanban_android.domain.model.NamedData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicScreen(
    topAppTitle: String = "",
    topAppActions: @Composable (RowScope.() -> Unit) = {},
    content: @Composable (PaddingValues) -> Unit

) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = topAppTitle,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                },
                actions = topAppActions,
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.secondary)

            )
        }

    ) { innerpadding ->
            content(innerpadding)

    }
}

@Composable
fun <T : NamedData> SelectableLazyColumn(
    modifier: Modifier = Modifier,
    items: List<T>,
    onItemSelected: (T) -> Unit,
    title: String = ""
) {
    Column() {
        if (title.isNotBlank()) {
            Card(
                modifier = modifier.padding(16.dp),
                elevation = CardDefaults.elevatedCardElevation(),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondary),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Text(
                    modifier = modifier.padding(10.dp),
                    text = title,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            items(items) { item ->
                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(120.dp),
                    onClick = { onItemSelected(item) },
                    elevation = CardDefaults.elevatedCardElevation(),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(
                        modifier = modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = item.name)
                    }
                }
            }
        }
    }

}


