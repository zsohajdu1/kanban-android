package com.example.kanban_android.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun ActionDialog(
    modifier: Modifier = Modifier,
    value: String,
    onNameChange: (String) -> Unit,
    onValueCreation: () -> Unit,
    valueIditentifier: String = "Name",
    onValueCreationChanged: (Boolean) -> Unit,
    actionName: String = "Create"
) {
    Dialog(onDismissRequest = { onValueCreationChanged(false) })
    {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(
                        modifier = modifier.fillMaxWidth(),
                        singleLine = true,
                        value = value,
                        onValueChange = onNameChange,
                        label = { Text(text = valueIditentifier) }
                    )
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(onClick = { onValueCreationChanged(false) }) {
                                Text(text = "Cancel")
                            }
                            Button(onClick = {
                                onValueCreation()
                                onValueCreationChanged(false)
                            }) {
                                Text(text = actionName)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConfimDialog(
    modifier: Modifier = Modifier,
    onConfirmation: () -> Unit,
    confirmationText: String = "Do you want to proceed",
    onConfirmationChanged: (Boolean) -> Unit,
    confirmButtonText: String = "Confirm"
) {
    Dialog(onDismissRequest = { onConfirmationChanged(false) })
    {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = modifier.fillMaxWidth(),
                        text = confirmationText
                    )
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(onClick = { onConfirmationChanged(false) }) {
                                Text(text = "Cancel")
                            }
                            Button(onClick = {
                                onConfirmation()
                                onConfirmationChanged(false)
                            }) {
                                Text(text = confirmButtonText)
                            }
                        }
                    }

                }
            }
        }
    }
}