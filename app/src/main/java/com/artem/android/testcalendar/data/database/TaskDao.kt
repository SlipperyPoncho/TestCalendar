package com.artem.android.testcalendar.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.artem.android.testcalendar.model.Task
import java.util.UUID

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    fun getTasks(): LiveData<List<Task>>
    @Query("SELECT * FROM task WHERE id=(:id)")
    fun getTask(id: UUID): LiveData<Task?>
    @Update
    fun updateTask(task: Task)
    @Insert
    fun addTask(task: Task)
    @Delete
    fun deleteTask(task: Task)
}