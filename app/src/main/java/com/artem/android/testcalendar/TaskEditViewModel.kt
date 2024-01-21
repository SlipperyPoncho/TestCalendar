package com.artem.android.testcalendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import java.util.UUID

class TaskEditViewModel: ViewModel() {
    private val taskRepository = TaskRepository.get()
    private val taskIdLiveData = MutableLiveData<UUID>()

    var taskLiveData: LiveData<Hour.Task?> = taskIdLiveData.switchMap {
        taskId -> taskRepository.getTask(taskId)
    }

    fun loadTask(taskId: UUID) {
        taskIdLiveData.value = taskId
    }

    fun updateTask(task: Hour.Task) {
        taskRepository.updateTask(task)
    }

    fun deleteTask(task: Hour.Task) {
        taskRepository.deleteTask(task)
    }
}