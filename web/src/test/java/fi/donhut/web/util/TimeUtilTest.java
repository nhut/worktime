/*
 * Copyright (c) from 2018 by Nhut Do.
 */

package fi.donhut.web.util;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link TimeUtil}.
 *
 * @author Nhut Do (mr.nhut@gmail.com)
 */
public class TimeUtilTest {

    @Test
    public void getTotalHour() {
        final int expectedHours = 2;
        final BigDecimal totalHourAndMinutes = new BigDecimal("2.5");

        final int result = TimeUtil.getTotalHour(totalHourAndMinutes);

        assertEquals(expectedHours, result);
    }

    @Test
    public void getTotalMinute() {
        final int expectedMinutes = 30;
        final BigDecimal totalHourAndMinutes = new BigDecimal("2.5");

        final int result = TimeUtil.getTotalMinute(totalHourAndMinutes,
                TimeUtil.getTotalHour(totalHourAndMinutes));

        assertEquals(expectedMinutes, result);
    }

    @Test
    public void getStartTime_returnsDefaultStartTime_notGoneOverADay() {
        final int defaultHour = 10;
        final int hoursToAdd = 5;

        final LocalTime result = TimeUtil.getStartTime(hoursToAdd);

        final int expectedStartTime = defaultHour;
        assertEquals(LocalTime.of(expectedStartTime, 0), result);
    }

    @Test
    public void getStartTime_returnsAdjustedStartTime_goneOverADay() {
        final int defaultHour = 10;
        final int hoursToAdd = 15;
        final int overHourForSpecialHandling = 13;

        final LocalTime result = TimeUtil.getStartTime(hoursToAdd);

        final int expectedStartTime = defaultHour - (hoursToAdd - overHourForSpecialHandling);
        assertEquals(LocalTime.of(expectedStartTime, 0), result);
    }

    @Test
    public void getEndTime() {
        LocalTime startTime = LocalTime.of(10, 0);
        int hoursToAdd = 8;
        int minutesToAdd = 30;

        final LocalTime result = TimeUtil.getEndTime(startTime, hoursToAdd, minutesToAdd);

        assertEquals(startTime.plusHours(hoursToAdd).plusMinutes(minutesToAdd), result);
    }
}