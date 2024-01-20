package com.artem.android.testcalendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

// TODO при добавлении задачи в тот час где уже есть задача в recycler старая заменяется на новую но
//  в tasksList остаются обе, надо либо заменять в tasksList либо сделать возможность добавлять
//  несколько тасков на один час.
// TODO Добавить несколько тасков при помощи JSON
// TODO Добавить возможность редакитровать задачу по нажатию на соответствующий элемент recycler
// TODO Room, JUnit, ViewModel, Убрать фиксированнй размер у календаря (календарь занимает весь
//  экран в landscape)

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

    override fun onNewTaskPressed(taskId: Int) {
        val taskEditFragment = TaskEditFragment.newInstance(taskId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, taskEditFragment)
            .addToBackStack(null)
            .commit()
    }
}