package com.artem.android.testcalendar

import android.app.Application

class TestCalendarApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        TaskRepository.initialize(this)
    }
}