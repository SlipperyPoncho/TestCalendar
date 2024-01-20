package com.artem.android.testcalendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap

class TaskEditViewModel: ViewModel() {
    private val taskRepository = TaskRepository.get()
    private val taskIdLiveData = MutableLiveData<Int>()

    var taskLiveData: LiveData<Hour.Task?> = taskIdLiveData.switchMap {
        taskId -> taskRepository.getTask(taskId)
    }

    fun loadTask(taskId: Int) {
        taskIdLiveData.value = taskId
    }

    fun saveTask(task: Hour.Task) {
        taskRepository.updateTask(task)
    }
}