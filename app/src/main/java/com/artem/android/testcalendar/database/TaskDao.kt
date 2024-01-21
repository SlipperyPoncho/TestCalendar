package com.artem.android.testcalendar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.artem.android.testcalendar.Hour
import java.util.UUID

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    fun getTasks(): LiveData<List<Hour.Task>>
    @Query("SELECT * FROM task WHERE id=(:id)")
    fun getTask(id: UUID): LiveData<Hour.Task?>
    @Update
    fun updateTask(task: Hour.Task)
    @Insert
    fun addTask(task: Hour.Task)
    @Delete
    fun deleteTask(task: Hour.Task)
}