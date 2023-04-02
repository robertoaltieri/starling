package com.altieri.starling.datetime.bl

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK

import org.junit.Before
import org.junit.Test
import java.util.Calendar
import kotlin.test.assertEquals

class AppDateTimeTest {

    @MockK
    private lateinit var calendarFactory: CalendarFactory

    private lateinit var dateTime: AppDateTime

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dateTime = AppDateTimeImpl(calendarFactory = calendarFactory)
    }

    @Test
    fun `oneWeekAgo returns the correct date with the correct format`() {
        // given
        every { calendarFactory.newInstance() } returns CALENDAR_TODAY

        // when
        val oneWeekAgo = dateTime.oneWeekAgo()

        // then
        assertEquals(ONE_WEEK_AGO, oneWeekAgo)
    }

    @Test
    fun `time returns the correct time`() {
        // given
        every { calendarFactory.newInstance() } returns CALENDAR_TODAY

        // when
        val time = dateTime.time()

        // then
        assertEquals(CALENDAR_TODAY.time.time, time)
    }

    private companion object {
        val CALENDAR_TODAY: Calendar = Calendar.getInstance().apply {
            set(2023, Calendar.APRIL, 8, 1, 1, 1)
            set(Calendar.MILLISECOND, 123)
        }
        const val ONE_WEEK_AGO = "2023-04-01T00:01:01.123Z"
    }
}
