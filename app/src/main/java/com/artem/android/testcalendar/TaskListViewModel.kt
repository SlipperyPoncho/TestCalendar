package com.artem.android.testcalendar

import androidx.lifecycle.ViewModel

class TaskListViewModel: ViewModel() {
    private val taskRepository = TaskRepository.get()
    val taskListLiveData = taskRepository.getTasks()

    fun addTask(task: Hour.Task) {
        taskRepository.addTask(task)
    }
}