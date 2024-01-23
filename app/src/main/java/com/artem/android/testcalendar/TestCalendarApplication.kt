package com.artem.android.testcalendar

import android.app.Application
import com.artem.android.testcalendar.domain.TaskRepository

class TestCalendarApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        TaskRepository.initialize(this)
    }
}