package com.artem.android.testcalendar

import java.time.LocalDate

interface OnItemListener {
    fun onItemClick(pos: Int, date: LocalDate)
}