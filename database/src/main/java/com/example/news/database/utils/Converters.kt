package com.example.news.database.utils

import android.icu.text.DateFormat
import androidx.room.TypeConverter
import java.util.Date

internal class Converters {

    @TypeConverter
    fun fromTimestamp(value: String?): Date? {
        return value?.let { DateFormat.getDateTimeInstance().parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): String? {
        return date?.time?.let { DateFormat.getDateTimeInstance().format(it) }
    }
}