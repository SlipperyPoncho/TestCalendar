package com.artem.android.testcalendar

import java.time.LocalDate
import java.time.LocalTime
import java.util.Date

data class Task(val id: Int = 0,
                var name: String = "",
                var dateStart: LocalDate = LocalDate.now(),
                var dateFinish: LocalDate = LocalDate.now(),
                var description: String = "") {

    var startTime: LocalTime = LocalTime.now()
    var finishTime: LocalTime = LocalTime.now()

    companion object {
        val tasksList: ArrayList<Task> = arrayListOf()

        fun tasksForDate(date: LocalDate): ArrayList<Task> {

            val tasks: ArrayList<Task> = arrayListOf()

            for (task in tasksList) {
                if (task.dateStart == date) {
                    tasks.add(task)
                }
            }

            return tasks
        }
    }
}
//class Task(id: Int, name: String, dateStart: LocalDate, dateFinish: LocalDate, description: String) {
//
//    private var id: Int
//    private var name: String
//    private var dateStart: LocalDate
//    private var dateFinish: LocalDate
//    private var description: String
//
//    init {
//        this.id = id
//        this.name = name
//        this.dateStart = dateStart
//        this.dateFinish = dateFinish
//        this.description = description
//    }
//
//    companion object {
//        val tasksList: ArrayList<Task> = arrayListOf()
//
//        fun tasksForDate(date: LocalDate): ArrayList<Task> {
//
//            val tasks: ArrayList<Task> = arrayListOf()
//
//            for (task in tasksList) {
//                if (task.dateStart == date) {
//                    tasks.add(task)
//                }
//            }
//
//            return tasks
//        }
//    }
//}