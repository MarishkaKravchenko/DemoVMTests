package com.example.demovmtests.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

abstract class DateFormatter {
    abstract val dateFormat: String

    private val calendar by lazy { Calendar.getInstance() }
    private val simpleDateFormat by lazy { SimpleDateFormat(dateFormat, Locale.US) }

    fun getFormattedDate(year: Int, month: Int, dayOfMonth: Int): String {
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        return simpleDateFormat.format(calendar.time)
    }

    fun getDate(date: String): Date? = try {
        simpleDateFormat.parse(date)
    } catch (exception: ParseException) {
        null
    }
}