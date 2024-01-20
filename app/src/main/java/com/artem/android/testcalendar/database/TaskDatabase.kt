package com.artem.android.testcalendar.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.artem.android.testcalendar.Hour

@Database(entities = [Hour.Task::class], version = 1)
@TypeConverters(TaskTypeConverters::class)
abstract class TaskDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
}