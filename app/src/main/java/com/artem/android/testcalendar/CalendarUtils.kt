package com.artem.android.testcalendar

import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class CalendarUtils {

    companion object {
        lateinit var selectedDate: LocalDate

        fun daysInMonthArray(date: LocalDate): ArrayList<LocalDate?> {
            val daysInMonthArray: ArrayList<LocalDate?> = arrayListOf()
            val yearMonth: YearMonth = YearMonth.from(date)
            val daysInMonth: Int = yearMonth.lengthOfMonth()
            val firstOfMonth: LocalDate = selectedDate.withDayOfMonth(1)
            val dayOfWeek: Int = firstOfMonth.dayOfWeek.value

            for (i in 2 until daysInMonth + dayOfWeek + 1) {
                if (i <= dayOfWeek) {
                    daysInMonthArray.add(null)
                }
                else {
                    daysInMonthArray.add(LocalDate.of(selectedDate.year, selectedDate.month, i - dayOfWeek))
                }
            }
            return daysInMonthArray
        }

        fun currentMonthFromDate(date: LocalDate): String {
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
            return date.format(formatter)
        }

        fun formattedDate(date: LocalDate): String {
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
            return date.format(formatter)
        }

        fun formattedTime(time: LocalTime): String {
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a")
            return time.format(formatter)
        }
    }
}