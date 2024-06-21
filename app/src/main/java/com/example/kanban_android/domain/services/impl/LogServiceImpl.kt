package com.example.kanban_android.domain.services.impl

import com.example.kanban_android.domain.services.base.LogService
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.Firebase
import javax.inject.Inject

class LogServiceImpl @Inject constructor() : LogService {
    override fun logNonFatalCrash(throwable: Throwable) =
        Firebase.crashlytics.recordException(throwable)
}