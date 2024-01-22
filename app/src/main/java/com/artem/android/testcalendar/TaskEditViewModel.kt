package com.artem.android.testcalendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class TaskEditViewModel: ViewModel() {
    private val taskRepository = TaskRepository.get()
    private val taskIdLiveData = MutableLiveData<UUID>()
    lateinit var task: Task
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    var taskLiveData: LiveData<Task?> = taskIdLiveData.switchMap {
        taskId -> taskRepository.getTask(taskId)
    }

    fun loadTask(taskId: UUID) {
        taskIdLiveData.value = taskId
    }

    fun updateTask(task: Task) {
        taskRepository.updateTask(task)
    }

    fun deleteTask(task: Task) {
        taskRepository.deleteTask(task)
    }

    fun formattedDate(date: LocalDateTime): String {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        return date.format(formatter)
    }
}