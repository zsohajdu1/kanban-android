package com.example.kanban_android.domain.model

data class Board(
    val id: String = "",
    override val name: String = "",
    val workspaceId: String = ""
) : NamedData