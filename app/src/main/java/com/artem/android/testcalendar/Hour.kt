package com.artem.android.testcalendar

import android.util.Range
import java.time.LocalDateTime
import java.time.LocalTime

data class Hour(var dateTime: LocalDateTime = LocalDateTime.now(),
                var task: Task? = null) {
    data class Task(
        val id: Int = 0,
        var name: String = "",
        var dateStart: LocalDateTime = LocalDateTime.now(),
        var dateFinish: LocalDateTime = LocalDateTime.now(),
        var description: String = "") {
        companion object {
            val tasksList: ArrayList<Task> = arrayListOf()
        }
    }
    companion object {
        fun setHours(): ArrayList<Hour> {
            val hoursList: ArrayList<Hour> = arrayListOf()
            for (i in 0 until 24) {
                val hour = Hour(LocalDateTime.of(CalendarUtils.selectedDate.toLocalDate(),
                        LocalTime.of(CalendarUtils.selectedDate.hour.plus(i),
                            CalendarUtils.selectedDate.minute)))
                for (task in Task.tasksList) {
                    if (task.dateStart.toLocalDate() == hour.dateTime.toLocalDate()) {
                        var range: Range<Int> = Range.create(task.dateStart.hour, task.dateFinish.hour)
                        if (task.dateFinish.minute == 0) {
                            range = Range.create(range.lower, range.upper.minus(1))
                        }
                        if (range.contains(hour.dateTime.hour)) {
                                hour.task = task
                        }
                    }
                }
                hoursList.add(hour)
            }
            return hoursList
        }
    }
}