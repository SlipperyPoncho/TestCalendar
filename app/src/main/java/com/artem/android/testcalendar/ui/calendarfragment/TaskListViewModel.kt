package com.artem.android.testcalendar.ui.calendarfragment

import android.content.Context
import androidx.lifecycle.ViewModel
import com.artem.android.testcalendar.model.TaskJSON
import com.artem.android.testcalendar.data.utils.readJSONFromAssets
import com.artem.android.testcalendar.domain.TaskRepository
import com.artem.android.testcalendar.model.Task
import com.google.gson.Gson
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.UUID

class TaskListViewModel: ViewModel() {
    private val taskRepository = TaskRepository.get()
    val taskListLiveData = taskRepository.getTasks()
    lateinit var selectedDate: LocalDateTime

    fun daysInMonthArray(date: LocalDateTime): ArrayList<LocalDate?> {
        val daysInMonthArray: ArrayList<LocalDate?> = arrayListOf()
        val yearMonth: YearMonth = YearMonth.from(date)
        val daysInMonth: Int = yearMonth.lengthOfMonth()
        val firstOfMonth: LocalDateTime? = selectedDate.withDayOfMonth(1)
        val dayOfWeek: Int? = firstOfMonth?.dayOfWeek?.value

        if (dayOfWeek != null) {
            for (i in 2 until daysInMonth + dayOfWeek + 1) {
                if (i <= dayOfWeek) {
                    daysInMonthArray.add(null)
                } else {
                    daysInMonthArray.add(
                        LocalDate.of(
                            selectedDate.year,
                            selectedDate.month,
                            i - dayOfWeek
                        )
                    )
                }
            }
        }
        return daysInMonthArray
    }

    fun currentMonthFromDate(date: LocalDateTime): String {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

    fun addTask(task: Task) {
        taskRepository.addTask(task)
    }

    fun taskFromJSON(context: Context): Task {
        val jsonString = readJSONFromAssets(context, "tasksData.json")
        val taskData = Gson().fromJson(jsonString, TaskJSON::class.java)

        return Task(
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