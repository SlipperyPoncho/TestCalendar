package com.artem.android.testcalendar

import android.content.Context
import androidx.lifecycle.ViewModel
import com.artem.android.testcalendar.utils.TaskJSON
import com.artem.android.testcalendar.utils.readJSONFromAssets
import com.google.gson.Gson
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

class TaskListViewModel: ViewModel() {
    private val taskRepository = TaskRepository.get()
    val taskListLiveData = taskRepository.getTasks()

    fun addTask(task: Hour.Task) {
        taskRepository.addTask(task)
    }

    fun taskFromJSON(context: Context): Hour.Task {
        val jsonString = readJSONFromAssets(context, "tasksData.json")
        val taskData = Gson().fromJson(jsonString, TaskJSON::class.java)

        return Hour.Task(
            UUID.fromString(taskData.id),
            taskData.name,
            LocalDateTime.ofInstant(
                Instant.ofEpochMilli(taskData.dateStart.toLong()),
                ZoneId.systemDefault()
            ),
            LocalDateTime.ofInstant(
                Instant.ofEpochMilli(taskData.dateFinish.toLong()),
                ZoneId.systemDefault()
            ),
            taskData.description
        )
    }
}