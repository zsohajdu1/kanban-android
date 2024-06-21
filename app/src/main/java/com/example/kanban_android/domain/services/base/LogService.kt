package com.example.kanban_android.domain.services.base

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}