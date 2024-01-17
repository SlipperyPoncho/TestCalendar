package com.artem.android.testcalendar

import java.time.LocalDateTime

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
}