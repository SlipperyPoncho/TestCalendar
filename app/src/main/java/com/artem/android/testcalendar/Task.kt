package com.artem.android.testcalendar

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.UUID

@Entity
data class Task(
    @PrimaryKey var id: UUID = UUID.randomUUID(),
    var name: String = "",
    var dateStart: LocalDateTime = LocalDateTime.now(),
    var dateFinish: LocalDateTime = LocalDateTime.now().plusHours(1),
    var description: String = "")