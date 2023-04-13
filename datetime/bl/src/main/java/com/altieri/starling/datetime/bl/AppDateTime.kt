package com.altieri.starling.datetime.bl

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

interface AppDateTime {
    fun oneWeekAgo(): String
    fun time(): Long
}

class AppDateTimeImpl(
    private val calendarFactory: CalendarFactory
) : AppDateTime {
    override fun oneWeekAgo(): String =
        calendarFactory
            .newInstance()
            .apply {
                add(Calendar.DAY_OF_YEAR, -7)
            }
            .time
            .time
            .toBackendFormat()

    private fun Long.toBackendFormat(): String {
        val formatter: DateFormat = backendDateFormat()
        val date = Date(this)
        return formatter.format(date)
    }

    private fun backendDateFormat(): DateFormat {
        val formatter: DateFormat = SimpleDateFormat(BACKEND, Locale.ROOT)
        formatter.timeZone = TIMEZONE
        return formatter
    }

    override fun time(): Long {
        val calendar = calendarFactory
            .newInstance()
        return calendar.time.time
    }

    private companion object {
        val TIMEZONE: TimeZone = TimeZone.getTimeZone("UTC")
        const val BACKEND = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    }
}

interface CalendarFactory {
    fun newInstance(): Calendar = Calendar.getInstance()
}
