package com.artem.android.testcalendar

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.artem.android.testcalendar.database.TaskDatabase
import java.lang.IllegalStateException
import java.util.concurrent.Executors

private const val DATABASE_NAME = "task-database"

class TaskRepository private constructor(context: Context) {

    private val database: TaskDatabase = Room.databaseBuilder(
        context.applicationContext,
        TaskDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val taskDao = database.taskDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getTasks(): LiveData<List<Hour.Task>> = taskDao.getTasks()
    fun getTask(id: Int): LiveData<Hour.Task?> = taskDao.getTask(id)
    fun updateTask(task: Hour.Task) {
        executor.execute {
            taskDao.updateTask(task)
        }
    }
    fun addTask(task: Hour.Task) {
        executor.execute {
            taskDao.addTask(task)
        }
    }

    companion object {
        private var INSTANCE: TaskRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) INSTANCE = TaskRepository(context)
        }
        fun get(): TaskRepository {
            return INSTANCE ?: throw IllegalStateException("TaskRepository must be initialized")
        }
    }
}