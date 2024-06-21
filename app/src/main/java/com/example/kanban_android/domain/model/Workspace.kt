package com.example.kanban_android.domain.model

data class Workspace(
    val id: String = "",
    override val name: String = ""
) : NamedData
