package com.artem.android.testcalendar.database

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

class TaskTypeConverters {
    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): Long? {
        val instant: Instant? = dateTime?.atZone(ZoneId.systemDefault())?.toInstant()
        if (instant != null) {
            return instant.toEpochMilli()
        }
        return null
    }

    @TypeConverter
    fun toLocalDateTime(epochMilli: Long?): LocalDateTime? {
        return LocalDateTime.ofInstant(epochMilli?.let { Instant.ofEpochMilli(it) },
            ZoneId.systemDefault())
    }

    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }
}