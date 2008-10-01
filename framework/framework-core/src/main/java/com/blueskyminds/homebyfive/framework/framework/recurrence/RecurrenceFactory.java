package com.blueskyminds.homebyfive.framework.framework.recurrence;

import com.blueskyminds.homebyfive.framework.framework.datetime.PeriodTypes;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Creates an appropriate instance of a Recurrence based on parameters provided to the factory method
 *
 * Date Started: 12/05/2006
 *
 * History:
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class RecurrenceFactory {

    /**
     * Create a recurrence commencing on the given start date with the specified standard period
     *
     * @param startDate     epoch for the recurrence
     * @param period
     * @return a recurrence
     */
    public static Recurrence create(Date startDate, PeriodTypes period) {

        Recurrence recurrence = null;

        switch (period) {
            case Month:
                Calendar cal = new GregorianCalendar();
                cal.setTime(startDate);
                int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                recurrence = new MonthlyRecurrence(dayOfMonth);
                break;
            case OnceOff:
                recurrence = new OnceOffRecurrence(startDate);
                break;
            default:
                throw new UnsupportedOperationException("No recurrence pattern exists for the requested period yet");
        }


        return recurrence;
    }

    /**
     * Create a recurrence commencing on the given start date with the specified period
     *
     * @param startTimestamp     epoch for the recurrence
     * @return a recurrence
     */
    public static Recurrence create(Date startTimestamp, int hours, int minutes) {

        Recurrence recurrence = null;

        recurrence = new HourlyRecurrence(startTimestamp, hours, minutes);

        return recurrence;
    }
}
