package com.artem.android.testcalendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.UUID

class MainActivity : AppCompatActivity(), CalendarFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment == null) {
            val calendarFragment = CalendarFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, calendarFragment)
                .commit()
        }
    }

    override fun onNewTaskPressed(taskId: UUID) {
        val taskEditFragment = TaskEditFragment.newInstance(taskId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, taskEditFragment)
            .addToBackStack(null)
            .commit()
    }
}