package com.artem.android.testcalendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

// TODO Landscape view (Calendar has fixed height), Add ViewModel to stay on selected date,
//  Add JSON Task, start and finish EditText can get wrong input (maybe add всплывашки для выбора
//  часа и минуты), Add task description to TaskEditFragment, organize code

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

    override fun onNewTaskPressed() {
        val taskEditFragment = TaskEditFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, taskEditFragment)
            .addToBackStack(null)
            .commit()
    }
}