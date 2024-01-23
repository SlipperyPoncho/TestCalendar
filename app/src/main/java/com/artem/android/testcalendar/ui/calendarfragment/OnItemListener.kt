package com.artem.android.testcalendar.ui.calendarfragment

import java.time.LocalDate

interface OnItemListener {
    fun onItemClick(pos: Int, date: LocalDate)
}