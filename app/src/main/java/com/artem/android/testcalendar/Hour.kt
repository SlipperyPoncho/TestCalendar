package com.artem.android.testcalendar

import android.util.Range
import java.time.LocalDateTime
import java.time.LocalTime

data class Hour(var dateTime: LocalDateTime = LocalDateTime.now(),
                var task: Task? = null) {
    companion object {
        fun setHours(dateTime: LocalDateTime, tasks: MutableList<Task>): List<Hour> {
            val hoursList: MutableList<Hour> = mutableListOf()
            for (i in 0 until 24) {
                val hour = Hour(LocalDateTime.of(dateTime.toLocalDate(),
                    LocalTime.of(dateTime.hour.plus(i), dateTime.minute)))
                for (task in tasks) {
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

        fun stringTime(time: Int): String {
            var timeStr = time.toString()
            if (timeStr.toInt() < 10) {
                timeStr = "0$timeStr"
            }
            return timeStr
        }
    }
}